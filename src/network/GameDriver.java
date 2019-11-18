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
            p1in = new Scanner(player1.getInputStream());
            p1out = new PrintWriter(player1.getOutputStream());
            p2in = new Scanner(player2.getInputStream());
            p2out = new PrintWriter(player2.getOutputStream());
        } catch (IOException ignored) {
        }
        while (gameState.movingColor != EMPTY) {
            int move;
            if (gameState.movingColor == BLACK) {
                move = getMove(p1in, p1out);
            } else {
                move = getMove(p2in, p2out);
            }
            gameState = gameState.makeMove(move);
            sendBoard();
        }
    }

    private int getMove(Scanner sc, PrintWriter out) {
        int move = -1;
        while (!gameState.isLegalMove(move)) {
            out.println("get_move " + gameState.movingColor);
            move = sc.nextInt();
        }
        return move;
    }

    private void sendBoard() {
        StringBuilder sb  = new StringBuilder("update_board ");
        for (Color color : gameState.board) {
            sb.append(color);
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length()-1); // get rid of the trailing space
        p1out.println(sb.toString());
        p2out.println(sb.toString());
    }
}
