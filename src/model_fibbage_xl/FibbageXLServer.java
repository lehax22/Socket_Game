package model_fibbage_xl;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Alex on 18.12.2016.
 */
public class FibbageXLServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server is running!");

        try {
            while(true) {
                Game game = new Game();

                Game.Player player = game.new Player(serverSocket.accept());
                System.out.println("Player is running!");
                Game.Player player1 = game.new Player(serverSocket.accept());
                System.out.println("Player1 is running!");

                player.setOpponent(player1);
                game.players.add(player);
                player1.setOpponent(player);
                game.players.add(player1);

                game.questionses = Game.readQuestions();

                player.start();
                player1.start();
            }
        } finally {
            serverSocket.close();
        }

    }

}
