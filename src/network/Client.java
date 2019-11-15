package network;

import gui.Disk;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int[] whiteBoard = new int[64];
    private int[] blackBoard = new int[64];
    private Disk currentDisk;

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public Client(String serverAddress) throws Exception {
        socket = new Socket(serverAddress, 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void play() throws Exception {
        try {
            System.out.println("Hello");
            String response = in.nextLine();

            while (in.hasNextLine()) {
                response = in.nextLine();
                if (response.startsWith("VALID_MOVE")) {
                } else if (response.startsWith("OPPONENT_MOVED")) {
                } else if (response.startsWith("MESSAGE")) {
                } else if (response.startsWith("VICTORY")) {
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    break;
                } else if (response.startsWith("TIE")) {
                    break;
                } else if (response.startsWith("OTHER_PLAYER_LEFT")) {
                    break;
                }
            }
            out.println("QUIT");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("127.0.0.1");
        client.play();
    }
}
