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
    private Disk selected;

    public Board(int width, int height, Dimension windowSize) {
        board = new int[height * width];
        tiles = new Rectangle[64];
        disks = new Circle[64];
        tileSize = new Dimension(windowSize.width / width, windowSize.height / height);

        // Used to keep track of the color of the tile.
        boolean color = true;

        // Set up the initial disks on the game board.
        board[27] = 1;
        board[28] = 2;
        board[35] = 2;
        board[36] = 1;

        // Iterate through and draw each tile.
        for (int i = 0; i < board.length; ++i) {
            int x = i % 8;
            int y = i / 8;

            Rectangle rectangle = new Rectangle(tileSize.width * x, tileSize.height * y, tileSize.width, tileSize.height);
            rectangle.setFill(color ? Color.GREEN : Color.OLIVE);
            Tile tile = new Tile(rectangle, new Point(x, y));
            /*
            tile.getRectangle().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                if (selected != null) {
                    // Implement whether there is a valid move or not.

                    // Checks if they player attempts to move to the same tile.
                    if (selected.getPosition().equals(tile.getPosition())) {
                        Game.getInstance().setStageTitle("Invalid Move");
                    }
                }

                Game.getInstance().update();
            });
            */
            tiles[i] = rectangle;

            // Set the disk position in the center of a tile, set the color of the disk based on board value, track the position in the board.
            Disk disk = new Disk(new Circle(tileSize.width * x + tileSize.width / 2, tileSize.height * y + tileSize.height / 2, (tileSize.width + tileSize.height) / 5, board[i] == 0 ? (color ? Color.GREEN : Color.OLIVE) : (board[i] == 1 ? Color.WHITE : Color.BLACK)), new Point(x, y));
            disk.getCircle().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                if (disk.getCircle().getFill().equals(Game.getInstance().getPlayer())) {
                    Game.getInstance().setStageTitle("Selected: X" + disk.getPosition().x + " Y" + disk.getPosition().y);
                    selected = disk;
                } else {
                    Game.getInstance().setStageTitle("Invalid Selection");
                }

                Game.getInstance().update();
            });
            disks[i] = disk.getCircle();

            // Used for alternating the tile color along the row.
            color = !color;
            // Used for alternating the tile color along the column.
            color = i % 8 == 7 != color;
        }
    }

    public Rectangle[] getTiles() {
        /*
        Set<Rectangle> rectangles = new HashSet<>();
        for (Tile tile : tiles) {
            rectangles.add(tile.getRectangle());
        }

        return rectangles;
        */
        return tiles;
    }

    public Circle[] getDisks() {
        /*
        Set<Circle> circles = new HashSet<>();
        for (Disk disk : disks) {
            if (disk != null) {
                circles.add(disk.getCircle());
            }
        }

        return circles;
        */
        return disks;
    }

    public Disk getSelected() {
        return selected;
    }
}
