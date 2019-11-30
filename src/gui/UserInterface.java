package gui;

import engine.Color;
import engine.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Optional;

import static engine.Color.*;

public class UserInterface {
    private Text movingText;
    private Text blackCounter;
    private Text whiteCounter;
    private Circle blackCircle;
    private Circle whiteCircle;
    private ObservableList ui;

    private final int padding = 10;

    /**
     * Construct the UI.
     *
     * @param windowWidth  window width
     * @param windowHeight window height
     * @param height       the height of the UI section
     */
    public UserInterface(int windowWidth, int windowHeight, int height) {
        movingText = new Text(padding, windowHeight + height / 2 + 0.75 * padding, "");
        movingText.setStyle("-fx-font: " + padding * 2 + " arial;");
        blackCircle = new Circle(windowWidth * 3 / 4 + height * 9 / 20, windowHeight + height / 2, height * 9 / 20);
        whiteCircle = new Circle(windowWidth * 7 / 8 + blackCircle.getRadius(), blackCircle.getCenterY(), blackCircle.getRadius() - 1);
        whiteCircle.setFill(javafx.scene.paint.Color.WHITE);
        whiteCircle.setStrokeWidth(1.0);
        whiteCircle.setStroke(Paint.valueOf("BLACK"));
        blackCounter = new Text(blackCircle.getCenterX() + padding * 2, windowHeight + height / 2 + 0.75 * padding, "");
        blackCounter.setStyle("-fx-font: " + padding * 2 + " arial;");
        whiteCounter = new Text(whiteCircle.getCenterX() + padding * 2, blackCounter.getY(), "");
        whiteCounter.setStyle("-fx-font: " + padding * 2 + " arial;");

        ui = FXCollections.observableArrayList();
        ui.addAll(movingText, blackCounter, whiteCounter, blackCircle, whiteCircle);
    }

    /**
     * Update the user interface so that the number of disks per player are displayed correctly.
     *
     * @param game the game
     */
    public void update(Game game) {

        movingText.setText((game.state.movingColor == BLACK ? "BLACK" : "WHITE") + " IS MOVING");

        int white = 0;
        int black = 0;

        for (Color color : game.state.board.toBoxBoard()) {
            if (color.equals(BLACK)) {
                ++black;
            } else if (color.equals(WHITE)) {
                ++white;
            }
        }

        blackCounter.setText("" + black);
        whiteCounter.setText("" + white);

        // Check to see if the game is over, is so prompt the player on what they want to do to continue.
        if (game.state.movingColor == EMPTY) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("CS 211-H01 Final Project");
            alert.setHeaderText("Othello Game - Dylan Jones, Minh Vu");
            alert.setContentText("Game Over: Black: " + black + " White: " + white);

            ButtonType playAgain = new ButtonType("Play Again");
            ButtonType cancel = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(playAgain, cancel);

            // Process the player's response to the prompt.
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().equals(playAgain)) {
                game.state = State.getStartingState();
                game.update();
            } else {
                System.exit(0);
            }
        }
    }

    /**
     * Get the UI for drawing.
     *
     * @return the list of all UI components
     */
    public ObservableList getUI() {
        return ui;
    }
}
