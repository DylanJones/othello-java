import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;

public class Game {
    private Stage stage;
    private Scene scene;
    private Pane pane;

    private Dimension windowSize;
    private Board board;

    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight) {
        this.stage = stage;
        this.stage.setTitle("Othello Java - Dylan Jones, Minh Vu");
        this.stage.resizableProperty().setValue(false);
        pane = new Pane();
        windowSize = new Dimension(windowWidth, windowHeight);
        board = new Board(boardWidth, boardHeight, windowSize);
        pane.getChildren().addAll(board.getTiles());
        pane.getChildren().addAll(board.getDisks());
        scene = new Scene(pane, windowSize.width, windowSize.height);
        stage.setScene(scene);
        stage.show();
    }
}
