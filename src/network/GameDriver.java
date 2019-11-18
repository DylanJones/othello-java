package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static engine.Color.*;

import engine.Color;
import engine.State;

public class GameDriver implements Runnable {
    private Socket player1;
    private Socket player2;
    private State gameState;
    Scanner p1in;
    PrintWriter p1out;
    Scanner p2in;
    PrintWriter p2out;

    public boolean addPlayer(Socket s) {
        if (player1 == null) {
            player1 = s;
            return false;
        }
        player2 = s;
        return true;
    }

    @Override
    public void run() {
        // p1 is black, p2 is white
        gameState = State.getStartingState();
        try {
            Thread.sleep(500); // give the guis time to initalize
            p1in = new Scanner(player1.getInputStream());
            p1out = new PrintWriter(player1.getOutputStream());
            p2in = new Scanner(player2.getInputStream());
            p2out = new PrintWriter(player2.getOutputStream());
        } catch (IOException | InterruptedException ignored) {
        }
        while (gameState.movingColor != EMPTY) {
            int move;
            if (gameState.movingColor == BLACK) {
                System.out.println("Waiting for Black...");
                move = getMove(p1in, p1out);
            } else {
                System.out.println("Waiting for White...");
                move = getMove(p2in, p2out);
            }
            System.out.println("Sending moves....");
            sendMove(move);
            gameState = gameState.makeMove(move);
        }
    }

    private int getMove(Scanner sc, PrintWriter out) {
        int move = -1;
        while (!gameState.isLegalMove(move)) {
            out.println("get_move " + gameState.movingColor);
            out.flush();
            System.out.println("sent cmd");
            move = sc.nextInt();
        }
        System.out.println("We got our response back from " + gameState.movingColor + " - it was move #" + move);
        return move;
    }

    private void sendMove(int move) {
        String line = "move_made " + move;
        p1out.println(line);
        p2out.println(line);
        p1out.flush();
        p2out.flush();
    }
}
