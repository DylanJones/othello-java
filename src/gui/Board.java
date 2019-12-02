package gui;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static engine.Color.*;

public class Board {
    private Dimension tileSize;
    private final Rectangle[] tiles;
    private final Disk[] disks;
    private final Game parent;
    private final int animationTime = 1000;
    private long lastMouseClick;
    private boolean flipping = false;

    /**
     * Initialize all variables, create all disks and tiles, and set up the game board with the initial start pieces.
     *
     * @param width      the width of the game board
     * @param height     the height of the game board
     * @param windowSize the size of the game window
     */
    public Board(Game parent, int width, int height, Dimension windowSize) {
        // Initialize everything
        this.parent = parent;
        int length = height * width;
        tiles = new Rectangle[length];
        disks = new Disk[length];
        lastMouseClick = System.currentTimeMillis();

        tileSize = new Dimension(windowSize.width / width, windowSize.height / height);

        // Used to keep track of the color of the tile
        boolean color = true;

        // Iterate through and draw each tile.
        engine.Color[] board = parent.state.board.toBoxBoard();
        for (int i = 0; i < board.length; ++i) {
            int x = i % 8;
            int y = i / 8;

            // Create the tile and set the appropriate color.
            Rectangle rectangle = new Rectangle(tileSize.width * x, tileSize.height * y, tileSize.width, tileSize.height);
            rectangle.setFill(color ? Color.GREEN : Color.OLIVE);
            tiles[i] = rectangle;
            // Used for alternating the tile color along the row.
            color = !color;
            // Used for alternating the tile color along the column.
            color = i % height == (height - 1) != color;

            // Create a disk object that is drawn in the center of the corresponding board tile. Associate the index to signify the position of the tile on the board.
            Disk disk = new Disk(new Circle(tileSize.width * x + tileSize.width / 2, tileSize.height * y + tileSize.height / 2, (tileSize.width + tileSize.height) / 4.5), i);
            // Add an event for when the mouse clicks on the disk.
            disk.getCircle().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                // Prevent the player from clicking if it is too soon or there is still a tile moving or double click weird case.
                if ((System.currentTimeMillis() - lastMouseClick < animationTime) && (!(disk.getCircle().getFill().equals(Color.OLIVE) || disk.getCircle().getFill().equals(Color.GREEN)) || flipping)) {
                    return;
                }

                // Ensure that the selected location of the player is an open tile.
                if (disk.getCircle().getFill().equals(Color.OLIVE) || disk.getCircle().getFill().equals(Color.GREEN)) {
                    // If it is a valid move, update the board and switch the player's color.
                    if (parent.state.isLegalMove(disk.getIndex()) && (parent.state.movingColor == parent.getPlayerColor() || parent.isLocalMultiplayer())) {
                        long timeElapsed = System.currentTimeMillis();

                        // Prevents player from moving before animation is complete.
                        if (parent.isLocalMultiplayer()) {
                            System.out.println("Disc got a valid local multiplayer move!");
                            parent.state = parent.state.makeMove(disk.getIndex());
                            System.out.println(parent.state);
                        } else if (parent.isOnline()) {
                            System.out.println("Disc got a valid online multiplayer move!");
                            parent.client.makeMove(disk.getIndex());
                        } else {// AI game
                            System.out.println("Disc got a valid move against an AI!");
                            parent.state = parent.state.makeMove(disk.getIndex());
                            System.out.println(parent.state);
                            new Thread(() -> {
                                var move = parent.ai.findMove(parent.state, parent.state.movingColor);
//                                var move = parent.ai.findMove(parent.state, parent.playerColor.invert());
                                waitForMove(timeElapsed);
                                parent.state = parent.state.makeMove(move);
                                Platform.runLater(parent::update);
                            }).start();
                        }
                    }
                }
                lastMouseClick = System.currentTimeMillis();
                parent.update();
            });
            disks[i] = disk;
        }
    }

    private void waitForMove(long timeElapsed) {
        long timeDifference = System.currentTimeMillis() - timeElapsed;
        System.out.println(timeDifference);
        if (timeDifference < animationTime) {
            try {
                Thread.sleep(animationTime - timeDifference);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Visually flip a disk from black to white - vice versa.
     */
    private void flip(Disk disk) {
        // Prevents the flipping the disk while still flipping.
        flipping = true;
        RotateTransition flip = new RotateTransition(Duration.millis(animationTime / 2.0), disk.getCircle());
        flip.setAxis(Rotate.Y_AXIS);
        flip.setFromAngle(0);
        flip.setToAngle(90);
        flip.setInterpolator(Interpolator.LINEAR);
        flip.play();
        flip.setOnFinished(e -> {
            disk.getCircle().setFill(disk.getCircle().getFill().equals(Color.BLACK) ? Color.WHITE : Color.BLACK);
            flip.setFromAngle(90);
            flip.setToAngle(180);
            flip.setOnFinished(f -> flipping = false);
            flip.play();
        });
    }


    /**
     * Change all the colors of the tiles according to state
     */
    public void update() {
        engine.Color[] board = parent.state.board.toBoxBoard();
        for (int i = 0; i < disks.length; ++i) {
            if (parent.state.isLegalMove(i)) {
                disks[i].getCircle().setStrokeWidth(2.0);
                disks[i].getCircle().setStroke(parent.state.movingColor == BLACK ? Color.BLACK : Color.WHITE);
            } else {
                disks[i].getCircle().setStrokeWidth(0);
            }

            if (board[i] == EMPTY) {
                disks[i].getCircle().setFill(tiles[i].getFill());
            } else if (board[i] == WHITE) {
                if (disks[i].getCircle().getFill().equals(Color.BLACK)) {
                    flip(disks[i]);
                } else {
                    disks[i].getCircle().setFill(Color.WHITE);
                }
            } else if (board[i] == BLACK) {
                if (disks[i].getCircle().getFill().equals(Color.WHITE)) {
                    flip(disks[i]);
                } else {
                    disks[i].getCircle().setFill(Color.BLACK);
                }
            }
        }
    }

    /**
     * @return the game board's tiles
     */
    public Rectangle[] getTiles() {
        return tiles;
    }

    /**
     * @return the game board's disks
     */
    public List<Circle> getDisks() {
        return Arrays.stream(disks).map(Disk::getCircle).collect(Collectors.toList());
    }
}
