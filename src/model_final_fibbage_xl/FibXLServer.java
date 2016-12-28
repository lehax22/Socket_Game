package model_final_fibbage_xl;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Alex on 19.12.2016.
 */
public class FibXLServer {

    private static final int PORT = 1234;

    public static void main(String[] args) {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running!");

            while (true) {

                int i = 1;

                FibXLGame fibXLGame = new FibXLGame();

                //game on 2 player
                while(true) {
                    FibXLGame.Player player = fibXLGame.new Player(serverSocket.accept());
                    if (i > 1) break;
                    i++;
                }

                System.out.println("Players play!");

                fibXLGame.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
