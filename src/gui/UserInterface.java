package gui;

import engine.Color;
import engine.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import static engine.Color.BLACK;
import static engine.Color.WHITE;

public class UserInterface {
    private Text movingText;
    private Text blackCounter;
    private Text whiteCounter;
    private Circle blackCircle;
    private Circle whiteCircle;
    private ObservableList ui;

    private final int padding = 10;

    public UserInterface(int windowWidth, int windowHeight, int height) {
        movingText = new Text(padding, windowHeight + height / 2 + 0.75 * padding, "");
        movingText.setStyle("-fx-font: " + padding * 2 + " arial;");
        blackCircle = new Circle(600 * 3 / 4 + height * 9 / 20, windowHeight + height / 2, height * 9 / 20);
        whiteCircle = new Circle(600 * 7 / 8 + blackCircle.getRadius(), blackCircle.getCenterY(), blackCircle.getRadius() - 1);
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

    public void update(State state) {
        movingText.setText((state.movingColor == BLACK ? "BLACK" : "WHITE") + " IS MOVING");

        int white = 0;
        int black = 0;

        for (Color color : state.board.toBoxBoard()) {
            if (color.equals(BLACK)) {
                ++black;
            } else if (color.equals(WHITE)) {
                ++white;
            }
        }

        blackCounter.setText("" + black);
        whiteCounter.setText("" + white);
    }

    public ObservableList getUI() {
        return ui;
    }
}
