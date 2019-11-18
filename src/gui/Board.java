package gui;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.awt.*;

import static engine.Color.EMPTY;
import static engine.Color.WHITE;

public class Board {
    private Dimension tileSize;
    private final Rectangle[] tiles;
    private final Circle[] disks;
    private final Game parent;

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
        disks = new Circle[length];
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

            // Some cool stuff.
//            RotateTransition rt = new RotateTransition(Duration.millis(1000), tiles[i]);
//            rt.setAxis(Rotate.Y_AXIS);
//            rt.setFromAngle(0);
//            rt.setToAngle(360);
//            rt.setInterpolator(Interpolator.LINEAR);
////            rt.setByAngle(180);
//            rt.setCycleCount(1);
////            rt.setAutoReverse(true);
//            rt.play();

            // Create a disk object that is drawn in the center of the corresponding board tile. Associate the index to signify the position of the tile on the board.
            Disk disk = new Disk(new Circle(tileSize.width * x + tileSize.width / 2, tileSize.height * y + tileSize.height / 2, (tileSize.width + tileSize.height) / 4.5), i);
            // Add an event for when the mouse clicks on the disk.
            disk.getCircle().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                // Ensure that the selected location of the player is an open tile.
                if (disk.getCircle().getFill().equals(Color.OLIVE) || disk.getCircle().getFill().equals(Color.GREEN)) {
                    // If it is a valid move, update the board and switch the player's color.
                    if (parent.state.isLegalMove(disk.getIndex()) &&
                            (parent.state.movingColor == parent.playerColor || !parent.isLocalMultiplayer())) {
                        if (parent.isLocalMultiplayer()) {
                            System.out.println("Disc got a valid local multiplayer move!");
                            parent.state = parent.state.makeMove(disk.getIndex());
                            System.out.println(parent.state);
                        } else if (parent.isOnline()) {
                            System.out.println("Disc got a valid online multiplayer move!");
                            parent.client.makeMove(disk.getIndex());
                        } else {// AI game
                        }
                    }
                }
                parent.update();
            });
            disks[i] = disk.getCircle();
        }
    }

    private void flip(Circle circle) {
        RotateTransition flip1 = new RotateTransition(Duration.millis(500), circle);
        flip1.setAxis(Rotate.Y_AXIS);
        flip1.setFromAngle(0);
        flip1.setToAngle(90);
        flip1.setInterpolator(Interpolator.LINEAR);
        flip1.play();
        flip1.setOnFinished(e -> {
            circle.setFill(circle.getFill().equals(Color.BLACK) ? Color.WHITE : Color.BLACK);
            flip1.setFromAngle(90);
            flip1.setToAngle(180);
            flip1.setOnFinished(f -> {
            });
            flip1.play();
        });
    }

    /**
     * Change all the colors of the tiles according to int board[];
     */
    public void update() {
        engine.Color[] board = parent.state.board.toBoxBoard();
        for (int i = 0; i < disks.length; ++i) {
            if (board[i] == EMPTY) {
                disks[i].setFill(tiles[i].getFill());
            } else if (board[i] == WHITE) {
                disks[i].setFill(Color.WHITE);
            } else {
                disks[i].setFill(Color.BLACK);
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
    public Circle[] getDisks() {
        return disks;
    }
}
