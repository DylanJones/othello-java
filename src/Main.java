import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
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

        Scene scene = new Scene(pane, windowSize.width, windowSize.height);
        stage.setScene(scene);
        stage.show();
    }
}
