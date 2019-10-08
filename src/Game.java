import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Dimension;

public class Game {
    private static Game instance;
    private Stage stage;
    private Scene scene;
    private Color player;

    public Group getRoot() {
        return root;
    }

    private Group root;

    private Dimension windowSize;
    private Board board;

    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight) {
        this.stage = stage;
        this.stage.setTitle("Othello Java - Dylan Jones, Minh Vu");
        this.stage.resizableProperty().setValue(false);
        this.stage.getIcons().add(new Image("file:res/icon.png"));
        root = new Group();
        windowSize = new Dimension(windowWidth, windowHeight);
        board = new Board(boardWidth, boardHeight, windowSize);
        root.getChildren().addAll(board.getTiles());
        root.getChildren().addAll(board.getDisks());
        scene = new Scene(root, windowSize.width, windowSize.height);
        stage.setScene(scene);
        stage.show();

        player = Color.BLACK;

        instance = this;
    }

    public static Game getInstance() {
        return instance;
    }

    public void setStageTitle(String title) {
        stage.setTitle(title);
    }

    public Color getPlayer() {
        return player;
    }

    public void update() {
        root.getChildren().clear();
        root.getChildren().addAll(board.getTiles());
        // Creates a red border around the selected disk.
        if (board.getSelected() != null) {
            root.getChildren().add(new Circle(board.getSelected().getCircle().getCenterX(), board.getSelected().getCircle().getCenterY(), board.getSelected().getCircle().getRadius() * 1.1, Color.RED));
        }
        root.getChildren().addAll(board.getDisks());
    }
}
