package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.Point;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Board {
    private int[] board;
    private Dimension tileSize;
    private Rectangle[] tiles;
    private Circle[] disks;

    public Board(int width, int height, Dimension windowSize) {
        int length = height * width;
        board = new int[length];
        tiles = new Rectangle[length];
        disks = new Circle[length];
        tileSize = new Dimension(windowSize.width / width, windowSize.height / height);

        // Set up the initial disks on the game board.
        board[27] = 1;
        board[28] = 2;
        board[35] = 2;
        board[36] = 1;

        // Used to keep track of the color of the tile.
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
            color = i % 8 == 7 != color;

            // Create a disk object that is drawn in the center of the corresponding board tile. Associate the index to signify the position of the tile on the board.
            Disk disk = new Disk(new Circle(tileSize.width * x + tileSize.width / 2, tileSize.height * y + tileSize.height / 2, (tileSize.width + tileSize.height) / 5), i);
            // Add an event for when the mouse clicks on the disk.
            disk.getCircle().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                // Ensure that the selected location of the player is an open tile.
                if (disk.getCircle().getFill().equals(Color.OLIVE) || disk.getCircle().getFill().equals(Color.GREEN)) {
                    // If it is a valid move, update the board and switch the player's color.
                    if (isValidMove()) {
                        board[disk.getIndex()] = Game.getInstance().getPlayer().equals(Color.BLACK) ? 2 : 1;
                        Game.getInstance().nextPlayer();
                    }
                }
                Game.getInstance().update();
            });
            disks[i] = disk.getCircle();
        }
    }

    private boolean isValidMove() {
        return true;
    }

    /**
     * Change all the colors of the tiles according to int board[];
     */
    public void update() {
        for (int i = 0; i < disks.length; ++i) {
            disks[i].setFill(board[i] == 0 ? tiles[i].getFill() : (board[i] == 1 ? Color.WHITE : Color.BLACK));
        }
    }

    public Rectangle[] getTiles() {
        return tiles;
    }

    public Circle[] getDisks() {
        return disks;
    }
}
