package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Game othello = new Game(stage, 600, 600, 8, 8);

        /*
        // Set the title of the window.
        stage.setTitle("Othello Java - Dylan Jones, Minh Vu");
        // Prevent the window from being resized.
        stage.resizableProperty().setValue(false);

        // Create a pane where the graphics will be drawn to.
        Pane pane = new Pane();

        // Define the dimensions.
        Dimension windowSize = new Dimension(600, 600);
        Dimension boardSize = new Dimension(8, 8);
        Dimension tileSize = new Dimension(windowSize.width / boardSize.width, windowSize.height / boardSize.height);

        // Used to keep track of the color of the tile.
        boolean color = true;

        // Iterate through and draw each tile.
        for (int i = 0; i < boardSize.getWidth(); ++i) {
            for (int j = 0; j < boardSize.getHeight(); ++j) {
                Rectangle rectangle = new Rectangle(tileSize.width * i, tileSize.height * j, tileSize.width, tileSize.height);
                rectangle.setFill(color ? Color.GREEN : Color.OLIVE);
                pane.getChildren().add(rectangle);
                // Used for alternating the color along the columns.
                color = !color;
            }
            // Used for alternating the color along the rows.
            color = !color;
        }

        int[][] board = new int[boardSize.height][boardSize.width];

        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                board[i][j] = 0;
            }
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
                    pane.getChildren().add(circle);
                }
            }
        }

        Scene scene = new Scene(pane, windowSize.width, windowSize.height);
        stage.setScene(scene);
        stage.show();
        */
    }
}
