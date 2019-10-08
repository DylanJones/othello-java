import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.Point;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Board {
    private int[][] board;
    private Dimension tileSize;
    private Set<Tile> tiles;
    private Set<Disk> disks;
    private HashMap<Tile, Disk> tileDiskHashMap;
    private Disk selected;

    public Board(int width, int height, Dimension windowSize) {
        board = new int[height][width];
        tiles = new HashSet<>();
        disks = new HashSet<>();
        tileSize = new Dimension(windowSize.width / width, windowSize.height / height);

        // Used to keep track of the color of the tile.
        boolean color = true;

        // Iterate through and draw each tile.
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                Rectangle rectangle = new Rectangle(tileSize.width * j, tileSize.height * i, tileSize.width, tileSize.height);
                rectangle.setFill(color ? Color.GREEN : Color.OLIVE);
                Tile tile = new Tile(rectangle, new Point(j, i));
                tile.getRectangle().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (selected != null) {
                            // Implement whether there is a valid move or not.

                            // Checks if they player attempts to move to the same tile.
                            if (selected.getPosition().equals(tile.getPosition())) {
                                Game.getInstance().setStageTitle("Invalid Move");
                            }
                        }

                        Game.getInstance().update();
                    }
                });
                tiles.add(tile);
                board[i][j] = 0;
                // Used for alternating the color along the rows.
                color = !color;
            }
            // Used for alternating the color along the column.
            color = !color;
        }

        // Set up the initial disks on the game board.
        board[3][3] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
        board[4][4] = 1;

        // Create the disks corresponding to the value on the game board.
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] != 0) {
                    // Set the disk position in the center of a tile, set the color of the disk based on board value, track the position in the board.
                    Disk disk = new Disk(new Circle(tileSize.width * j + tileSize.width / 2,tileSize.height * i + tileSize.height / 2, (tileSize.width + tileSize.height) / 5, board[i][j] == 1 ? Color.WHITE : Color.BLACK), new Point(j, i));
                    disk.getCircle().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            if (disk.getCircle().getFill().equals(Game.getInstance().getPlayer())) {
                                Game.getInstance().setStageTitle("Selected: X" + disk.getPosition().x + " Y" + disk.getPosition().y);
                                selected = disk;
                            } else {
                                Game.getInstance().setStageTitle("Invalid Selection");
                            }

                            Game.getInstance().update();
                        }
                    });
                    disks.add(disk);


                }
            }
        }
    }

    public Set<Rectangle> getTiles() {
        Set<Rectangle> rectangles = new HashSet<>();
        for (Tile tile : tiles) {
            rectangles.add(tile.getRectangle());
        }

        return rectangles;
    }

    public Disk getSelected() {
        return selected;
    }

    public Set<Circle> getDisks() {
        Set<Circle> circles = new HashSet<>();
        for (Disk disk : disks) {
            circles.add(disk.getCircle());
        }

        return circles;
    }
}
