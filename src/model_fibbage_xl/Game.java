package model_fibbage_xl;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 18.12.2016.
 */
public class Game {

    public List<PrintWriter> writers = new ArrayList<>();

    public List<Player> players = new ArrayList<>();
    public List<Questions> questionses = new ArrayList<>();
    public List<String> answers = new ArrayList<>();

    public Questions q;

    public static List<Questions> readQuestions() throws FileNotFoundException {
        Questions questions = new Questions();
        return questions.readQuestios();
    }

    public void sendQuestion() {
        q = questionses.get((int)Math.random()*(writers.size() + 1));
        while(!q.unread) {
            q = questionses.get((int)Math.random()*(writers.size() + 1));
        }
        q.unread = false;
        for (PrintWriter write: writers) {
            write.println("QUESTION " + q.question);
        }
        answers.add(q.answer);
    }

    public void sendAnswers() {
        for (PrintWriter write: writers) {

            write.println("WAY");

            for (String s: answers) {
                write.println(s);
            }
        }
    }

    public void checkAnswers(String response) {
        for (Player p: players) {
            if (response == q.answer) {
                p.score += 1000;
            } else {
                p.score -=100;
            }

            p.out.println("RESULT Your result");
            p.out.println(p.score);
        }
    }


    class Player extends Thread{

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        private String name;
        private int score;
        private Player opponent;

        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        public Player(Socket accept){

            this.socket = accept;

            try {
                in = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                out = new PrintWriter(accept.getOutputStream(), true);

                out.println("WELCOME IN THE GAME");

                this.name = in.readLine();
                writers.add(out);

                out.println("MESSAGE Waiting for opponents to connect");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {

            String response;

            out.println("MESSAGE-READY All players connected");

            while(true) {

                sendQuestion();

                try {
                    out.println("MESSAGE-ENTER Enter the answer");
                    response = in.readLine();
                    answers.add(response);

                    out.println("MESSAGE-SELECT Select answer");
                    sendAnswers();
                    response = in.readLine();

                    checkAnswers(response);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
