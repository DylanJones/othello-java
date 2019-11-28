package gui;

import engine.Color;
import engine.SearchAlgorithm;
import engine.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.Client;

import java.awt.*;

import static engine.Color.BLACK;
import static engine.Color.WHITE;

public class Game {
    private Stage stage;
    private Scene scene;
    public Color playerColor;
    public State state;

    private Group root;

    private Dimension windowSize;
    private Board board;

    private boolean localMultiplayer;
    private boolean online;

    public Client client;
    public SearchAlgorithm ai;

    private final int lowerUI = 30;
    private final int rightUI = 100;
    private final int padding = 10;

    private Text movingText;
    private ComboBox comboBox;

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
        stage.setScene(scene);
        stage.show();
        this.localMultiplayer = true;
        this.online = false;

        movingText = new Text(padding, windowHeight + padding * 2, "");
        movingText.setStyle("-fx-font: " + padding * 2 + " arial;");

        update();
    }

    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight, Client client) {
        this(stage, windowWidth, windowHeight, boardWidth, boardHeight);
        this.client = client;
        this.online = true;
        this.localMultiplayer = false;
        client.attachGame(this);
    }

    public Game(Stage stage, int windowWidth, int windowHeight, int boardWidth, int boardHeight, SearchAlgorithm ai) {
        this(stage, windowWidth, windowHeight, boardWidth, boardHeight);
        this.ai = ai;
        this.localMultiplayer = false;
        playerColor = WHITE;

        ObservableList<String> options = FXCollections.observableArrayList("Minimax", "Alphabeta");
        comboBox = new ComboBox(options);
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
        movingText.setText((state.movingColor == BLACK ? "BLACK" : "WHITE") + " IS MOVING");
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
        root.getChildren().add(movingText);

        if (!online && !localMultiplayer) {
            root.getChildren().add(comboBox);
        }
    }
}
