package model_final_fibbage_xl;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Created by Alex on 19.12.2016.
 */
public class FibXLGame extends Thread{

    private List<PrintWriter> writers = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<FibXLQuestion> fibXLQuestionList = new ArrayList<>();
    private HashMap<String, Player> answers = new HashMap<>();

    @Override
    public void run() {

        for (PrintWriter writer: writers) {
            writer.println("MESSAGE-START The game start");
        }

        FibXLQuestion f = new FibXLQuestion();
        fibXLQuestionList = f.readQues();

        //waiting
        try {

            for (int i = 3; i > 0; i--) {
                for (PrintWriter writer : writers) {
                    writer.println(i);
                }
                Thread.sleep(1000L);
            }

            for (PrintWriter writer: writers) {
                writer.println("Start");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //game
        for (int i = 0; i < 5; i++) {

            while (true) {
                f = fibXLQuestionList.get((int) (Math.random() * (fibXLQuestionList.size())));
                if (!f.unread) {
                    f.unread = true;
                    break;
                }
            }

            for(PrintWriter writer: writers) {

                writer.println("QUESTION Question");
                writer.println(f.question);
                writer.println("MESSAGE-W-W Write the wrong answer");

            }

            answers.put(f.answer, null);

            for (Player player: players) {
                try {
                    answers.put(player.in.readLine(), player);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (PrintWriter writer: writers) {

                writer.println("MESSAGE-W-A Choose the best answer");

                for (String s : answers.keySet()) {
                    writer.println(s);
                }

            }

            for (Player player: players) {

                try {
                    String an = player.in.readLine();
                    if (Objects.equals(an, f.answer)) {
                        player.score += 1000;
                    } else {
                        if (player.score >= 100) {
                            player.score -= 100;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.out.println("MESSAGE-RESULT Your score   " + player.score);

            }

            answers.clear();
            if (i != 4) {
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE-NEXT Next question!!!");
                }
            }

        }

        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.score - o1.score;
            }
        });

        Player pl = players.get(0);
        for (Player player: players) {
            if (pl.score == player.score) {
                player.out.println("MESSAGE-WIN You winner. Your score " + player.score);
            } else {
                player.out.println("MESSAGE-LOSE You lose. Your score " + player.score + ". The winner " + pl.name + " scored " + pl.score + " points.");
            }
        }

        for (PrintWriter writer: writers) {
            writer.println("QUIT Game end!");
        }

    }

    public class Player{

        Socket socket;
        BufferedReader in;
        PrintWriter out;

        String name;
        int score;

        public Player(BufferedReader in, PrintWriter out, String name, int score) {
            this.in = in;
            this.out = out;
            this.name = name;
            this.score = score;
        }

        public Player(Socket accept) {

            this.socket = accept;

            try {
                in = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                out = new PrintWriter(accept.getOutputStream(), true);

                out.println("MESSAGE-CONNECTED Please, enter the your NAME!");

                String name;

                while (true) {
                    name = in.readLine();

                    if (name != null) {
                        out.println("MESSAGE-WAIT Waiting for opponent to connect");
                        break;
                    } else {
                        out.println("MESSAGE-WARNING Excuse me, but you have not entered your name!");
                        out.println("Please, enter the your NAME!");
                    }
                }

                writers.add(out);
                players.add(new Player(in, out, name, 0));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /*@Override
        public int compareTo(Object o) {
            Player p = (Player) o;
            if (this.score < p.score) {
                return -1;
            } else if (this.score > p.score){
                return 1;
            }
            return 0;
        }*/
    }

    public class FibXLQuestion {

        String question;
        String answer;
        boolean unread;

        public FibXLQuestion() {}

        public FibXLQuestion(String question, String answer, boolean unread) {

            this.question = question;
            this.answer = answer;
            this.unread = unread;

        }

        public List<FibXLQuestion> readQues() {

            FileReader file;
            BufferedReader in;

            List<FibXLQuestion> fibXLQuestion;

            try {
                file = new FileReader("C:\\Users\\Alex\\IdeaProjects\\FibbageXL\\src\\ques.txt");
                in = new BufferedReader(file);
                fibXLQuestion = new ArrayList<>();

                String offer;

                while((offer = in.readLine()) != null) {
                    fibXLQuestion.add(new FibXLQuestion(offer, in.readLine(), false));
                }

                return fibXLQuestion;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
