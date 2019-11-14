package gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.util.Collection;

public class Game {
    private static Game instance;
    private Stage stage;
    private Scene scene;
    private Color player;

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

    public Board getBoard() {
        return board;
    }

    public Group getRoot() {
        return root;
    }

    // Call this once you have changed the disk colors to repaint the screen
    public void update() {
        stage.setTitle((player.equals(Color.BLACK) ? "BLACK" : "WHITE") + " IS MOVING");
        board.update();
        repaint();
    }

    // Clears the root and redraws the all the disks and tiles
    public void repaint() {
        root.getChildren().clear();
        root.getChildren().addAll(board.getTiles());
        root.getChildren().addAll(board.getDisks());
    }
}
