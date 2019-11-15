package gui;

import engine.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class Game {
    private static Game instance;
    private Stage stage;
    private Scene scene;
    private byte player;

    private Group root;

    private Dimension windowSize;
    private Board board;

    private boolean multiplayer;

    /**
     * Start the game and initialize everything
     *
     * @param stage        the JavaFX stage
     * @param windowWidth  the width of the window in pixels
     * @param windowHeight the height of the window in pixels
     * @param boardWidth   the number of tiles on the board horizontally
     * @param boardHeight  the number of tiles on the board vertically
     */
    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight, boolean multiplayer) {
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

        player = State.BLACK;
        this.multiplayer = multiplayer;

        instance = this;

        update();
    }

    /**
     * @return the game instance
     */
    public static Game getInstance() {
        return instance;
    }

    /**
     * @return whether the game is multiplayer
     */
    public boolean isMultiplayer() {
        return multiplayer;
    }

    /**
     * @return the player value
     */
    public byte getPlayer() {
        return player;
    }

    /**
     * Alternate between the black and white players
     */
    public void nextPlayer() {
        player = player == State.BLACK ? State.WHITE : State.BLACK;
    }

    /**
     * @param message the message to be displayed
     */
    public void setMessage(String message) {
        stage.setTitle(message);
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
