package gui;

import engine.State;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.awt.*;

public class Board {
    private State state;
    private int[] board;
    private Dimension tileSize;
    private Rectangle[] tiles;
    private Circle[] disks;

    /**
     * Initialize all variables, create all disks and tiles, and set up the game board with the initial start pieces.
     *
     * @param width      the width of the game board
     * @param height     the height of the game board
     * @param windowSize the size of the game window
     */
    public Board(int width, int height, Dimension windowSize) {
        // Initialize everything
        int length = height * width;
        board = new int[length];
        tiles = new Rectangle[length];
        disks = new Circle[length];
        tileSize = new Dimension(windowSize.width / width, windowSize.height / height);

        // Set up the initial disks on the game board
        board[27] = 1;
        board[28] = 2;
        board[35] = 2;
        board[36] = 1;

        // Used to keep track of the color of the tile
        boolean color = true;

        // Iterate through and draw each tile.
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

            state = State.fromBoxBoard(board, State.BLACK);

            // Create a disk object that is drawn in the center of the corresponding board tile. Associate the index to signify the position of the tile on the board.
            Disk disk = new Disk(new Circle(tileSize.width * x + tileSize.width / 2, tileSize.height * y + tileSize.height / 2, (tileSize.width + tileSize.height) / 4.5), i);
            // Add an event for when the mouse clicks on the disk.
            disk.getCircle().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                // Ensure that the selected location of the player is an open tile.
                if (disk.getCircle().getFill().equals(Color.OLIVE) || disk.getCircle().getFill().equals(Color.GREEN)) {
                    // If it is a valid move, update the board and switch the player's color.
                    if ((state.calcLegalMoves() & (1 << disk.getIndex())) != 0) {
//                    if (true) {
                        flip(disk.getCircle());
                        System.out.println(disk.getIndex());

//                        state = state.makeMove(disk.getIndex());
//                        System.out.println(state);

                        board[disk.getIndex()] = Game.getInstance().getPlayer();
                        Game.getInstance().nextPlayer();

                        if (!Game.getInstance().isMultiplayer()) {
                            // RUN AI TURN HERE. THEN CONTINUE
                        }
                    }
                }
                Game.getInstance().update();
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

    private boolean isValidMove(int index) {
        return true;
    }

    /**
     * Change all the colors of the tiles according to int board[];
     */
    public void update() {
//        board = state.toBoxBoard();
        System.out.println(state);
        for (int i = 0; i < disks.length; ++i) {
            if (board[i] == State.EMPTY) {
                disks[i].setFill(tiles[i].getFill());
            } else if (board[i] == State.WHITE) {
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
