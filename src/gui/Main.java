package gui;

import engine.Minimax;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import network.Client;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Create the menu and prompts the player with the choice of multiplayer or singleplayer
     *
     * @param stage the JavaFX stage
     */
    @Override
    public void start(Stage stage) {
        // Initialize the Menu prompt for the game.
        Alert alert = new Alert(AlertType.NONE);
        alert.setTitle("CS 211-H01 Final Project");
        alert.setHeaderText("Othello Game - Dylan Jones, Minh Vu");
        alert.setContentText("Please Select The Game Mode");

        // Add buttons for the player to select the game mode.
        ButtonType singleplayer = new ButtonType("Singleplayer");
        ButtonType multiplayer = new ButtonType("Local Multiplayer");
        ButtonType online = new ButtonType("Online Multiplayer");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(singleplayer, multiplayer, online, cancel);

        // Process the player's response to the prompt.
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(singleplayer)) {
            // Prompt player for color.
            alert.setContentText("Please Select Your Color");
            ButtonType black = new ButtonType("Black");
            ButtonType white = new ButtonType("White");
            alert.getButtonTypes().setAll(black, white, cancel);
            Optional<ButtonType> color_result = alert.showAndWait();

            if (color_result.get().equals(black)) {
                new Game(stage, 600, 600, 8, 8, new Minimax());
            } else if (color_result.get().equals(white)) {
                new Game(stage, 600, 600, 8, 8, new Minimax());
            }

            System.out.println("Starting a new singleplayer game...");
        } else if (result.get().equals(multiplayer)) {
            new Game(stage, 600, 600, 8, 8);
        } else if (result.get().equals(online)) {
            // Start the client and try to connect to the server.
            try {
                Client c = new Client("localhost");
                new Game(stage, 600, 600, 8, 8, c);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
