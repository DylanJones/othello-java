package gui;

import engine.Color;
import engine.SearchAlgorithm;
import engine.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import network.Client;

import java.awt.*;

public class Game {
    private Stage stage;
    private Scene scene;
    private Color playerColor;
    public State state;

    private Group root;

    private Dimension windowSize;
    private Board board;

    private boolean localMultiplayer;
    private boolean online;

    public Client client;
    public SearchAlgorithm ai;

    private final int lowerUI = 30;
    private final int rightUI = 0;

    private UserInterface userInterface;

    /**
     * Start the game and initialize everything
     *
     * @param stage        the JavaFX stage
     * @param windowWidth  the width of the window in pixels
     * @param windowHeight the height of the window in pixels
     * @param boardWidth   the number of tiles on the board horizontally
     * @param boardHeight  the number of tiles on the board vertically
     */
    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight) {
        this.stage = stage;
        this.stage.resizableProperty().setValue(false);
        this.stage.getIcons().add(new Image("file:res/icon.png"));
        this.stage.setTitle("Othello Java - Dylan Jones, Minh Vu");
        state = State.getStartingState();
        root = new Group();
        windowSize = new Dimension(windowWidth, windowHeight);
        board = new Board(this, boardWidth, boardHeight, windowSize);
        root.getChildren().addAll(board.getTiles());
        root.getChildren().addAll(board.getDisks());
        scene = new Scene(root, windowSize.width + rightUI, windowSize.height + lowerUI);
        userInterface = new UserInterface(windowWidth, windowHeight, lowerUI);
        stage.setScene(scene);
        stage.show();

        this.localMultiplayer = true;
        this.online = false;

        update();
    }

    /**
     * Constructor for online multiplayer.
     *
     * @param stage        the JavaFX stage
     * @param windowWidth  the width of the window in pixels
     * @param windowHeight the height of the window in pixels
     * @param boardWidth   the number of tiles on the board horizontally
     * @param boardHeight  the number of tiles on the board vertically
     * @param client       the player's client
     */
    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight, Client client) {
        this(stage, windowWidth, windowHeight, boardWidth, boardHeight);
        this.client = client;
        this.online = true;
        this.localMultiplayer = false;
        client.attachGame(this);
    }

    /**
     * Constructor for singleplayer vs ai.
     *
     * @param stage        the JavaFX stage
     * @param windowWidth  the width of the window in pixels
     * @param windowHeight the height of the window in pixels
     * @param boardWidth   the number of tiles on the board horizontally
     * @param boardHeight  the number of tiles on the board vertically
     * @param ai           the type of search algorithm used by the ai
     */
    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight, SearchAlgorithm ai) {
        this(stage, windowWidth, windowHeight, boardWidth, boardHeight);
        this.ai = ai;
        this.localMultiplayer = false;
        playerColor = Color.WHITE;
    }

    /**
     * @return whether the game is a local multiplayer game
     */
    public boolean isLocalMultiplayer() {
        return localMultiplayer;
    }


    /**
     * @return whether the game is online
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Used to update the game board with new positions and repaint()
     */
    public void update() {
        board.update();
        userInterface.update(state);
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
        root.getChildren().addAll(userInterface.getUI());
    }

    /**
     * Get the player's color
     * @return black of white
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * Set the player color
     * @param color black or white
     */
    public void setPlayerColor(Color color) {
        this.playerColor = color;
    }
}
