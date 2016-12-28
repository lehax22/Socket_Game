package test_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Alex on 11.12.2016.
 */
public class TestMain {

    public static List<Player> playerList = new ArrayList<>();
    public static HashMap<String, Player> answerPlayerHashMap = new HashMap<>();

    public static void main(String[] args) {

        Player player = new Player("Katya");
        Player player1 = new Player("Misha");
        playerList.add(player);
        playerList.add(player1);

        for (int i = 0; i < 5; i++) {

            //Theme Money || Question How much the money?
            System.out.println("How much the money?");

            //Player enter the false answer
            for (Player p: playerList) {
                p.getAnswer().fanswer = (new Scanner(System.in)).next();
            }

            for (Player p: playerList) {

                if (p.getAnswer().answer == "1000") {
                    p.addScore(1000);
                } else {
                    for (Player s: playerList) {
                        if (s.getAnswer().getFanswer() == p.getAnswer().answer) {
                            s.addScore(500);
                        }
                    }
                }

            }

        }
    }
}
