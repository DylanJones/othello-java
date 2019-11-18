package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(2020)) {
            ExecutorService pool = Executors.newFixedThreadPool(10);
            GameDriver g = new GameDriver();
            while (true) {
                Socket clientSocket = listener.accept();
                if (g.addPlayer(clientSocket)) {
                    System.out.println("Just got our second player - starting the game");
                    pool.execute(g);
                    g = new GameDriver();
                } else {
                    System.out.println("Got our first player");
                }
            }
        }
    }
}

