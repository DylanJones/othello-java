import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

public class Board {
    private int[][] board;
    private Dimension tileSize;
    private Set<Rectangle> tiles;
    private Set<Circle> disks;

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
                tiles.add(rectangle);
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
                    Circle circle = new Circle(tileSize.width * j + tileSize.width / 2, tileSize.height * i + tileSize.height / 2, (tileSize.width + tileSize.height) / 5, board[i][j] == 1 ? Color.WHITE : Color.BLACK);
                    disks.add(circle);
                }
            }
        }
    }

    public Set<Rectangle> getTiles() {
        return tiles;
    }

    public Set<Circle> getDisks() {
        return disks;
    }
}
