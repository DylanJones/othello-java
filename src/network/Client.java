package network;


import engine.Color;
import gui.Game;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Client implements Runnable {
    private Socket socket;
    private Game game;
    private Color ourColor;
    private int move;
    private volatile boolean waitingForMove = false;

    public Client(String serverAddr) throws IOException {
        socket = new Socket(serverAddr, 2020);
        // Listening/comm thread
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while (true) {
                var line = in.nextLine().strip().split(" ");
                var command = line[0];
                switch (command) {
                    case "get_move":
                        ourColor = Color.valueOf(line[1]);
                        game.playerColor = ourColor;
                        System.out.println("Getting ourselves a move - we are " + ourColor);
                        waitingForMove = true;
                        while (waitingForMove) {
                            Thread.onSpinWait();
                        }
                        System.out.println("We submitted our move! It was move #" + move);
                        out.println(move);
                        out.flush();
                        break;
                    case "move_made":
                        System.out.println("yo boiiii we just got a move @ spot " + line[1]);
                        if (game != null) {
                            game.state = game.state.makeMove(Integer.parseInt(line[1]));
                            Platform.runLater(game::update);
                        } else {
                            System.err.println("AAAAAAA GAME NOT INITIALIZED ABORT ABORT ABORT");
                            System.exit(23);
                        }
                        break;
//                    case "update_board":
//                        Color[] box = new Color[64];
//                        for (int i = 0; i < 64; i++) {
//                            box[i] = Color.valueOf(line[i + 1]);
//                        }
//                        if (game != null) {
//                            game.state =
//                        } else {
//                            System.err.println("AAAAAAA GAME NOT INITIALIZED ABORT ABORT ABORT");
//                            System.exit(23);
//                        }
//                        break;
                    default:
                        System.out.println("UNRECOGNIZED COMMAND: \"" + command + "\" - line = \"" + Arrays.toString(line) + "\"");
                }
            }
        } catch (IOException ignored) {
        }
    }

    public void attachGame(Game g) {
        this.game = g;
    }

    public void makeMove(int move) {
        this.move = move;
        this.waitingForMove = false;
    }
}
