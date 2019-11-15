package gui;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Alert alert = new Alert(AlertType.NONE);
        alert.setTitle("CS 211-H01 Final Project");
        alert.setHeaderText("Othello Game - Dylan Jones, Minh Vu");
        alert.setContentText("Please Select The Game Mode");

        ButtonType singleplayer = new ButtonType("Singleplayer");
        ButtonType multiplayer = new ButtonType("Multiplayer");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(singleplayer, multiplayer, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(singleplayer)) {
            Game othello = new Game(stage, 600, 600, 8, 8);
        } else if (result.get().equals(multiplayer)) {
            Game othello = new Game(stage, 600, 600, 8, 8);
        }
    }
}
