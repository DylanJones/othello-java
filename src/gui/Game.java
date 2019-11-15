package gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.Dimension;

public class Game {
    private static Game instance;
    private Stage stage;
    private Scene scene;
    private Player player;

    private Group root;

    private Dimension windowSize;
    private Board board;

    private enum Player {
        NONE,
        WHITE,
        BLACK,
    }

    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight) {
        this.stage = stage;
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

        player = Player.BLACK;

        instance = this;

        update();
    }

    public static Game getInstance() {
        return instance;
    }

    public int getPlayer() {
        return player.ordinal();
    }

    /**
     *
     */
    public void nextPlayer() {
        player = player.equals(Player.BLACK) ? Player.WHITE : Player.BLACK;
    }

    /**
     * Used to update the game board with new positions and repaint()
     */
    public void update() {
        stage.setTitle(player + " IS MOVING");
        board.update();
        repaint();
    }

    /**
     * Clear the root and add all tiles and disks back into it
     * Repaint will redraw everything on the game board
     */
    public void repaint() {
        root.getChildren().clear();
        root.getChildren().addAll(board.getTiles());
        root.getChildren().addAll(board.getDisks());
    }
}
