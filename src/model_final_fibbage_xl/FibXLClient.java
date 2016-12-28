package model_final_fibbage_xl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Alex on 19.12.2016.
 */
public class FibXLClient {

    public Scanner scanner = new Scanner(System.in);

    public final int PORT = 1234;

    BufferedReader in;
    PrintWriter out;

    String name;

    public FibXLClient(String localhost) {

        Socket socket;

        try {
            socket = new Socket(localhost, PORT);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String response;

                response = in.readLine();

                if (response.startsWith("MESSAGE-CONNECTED")) {
                    System.out.println(response.substring(18));
                    out.println(scanner.nextLine());
                } else if (response.startsWith("MESSAGE-WAIT")) {
                    System.out.println(response.substring(13));
                    break;
                } else if (response.startsWith("MESSAGE-WARNING")) {
                    System.out.println(response.substring(16));
                    System.out.println(in.readLine());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void play() {

        try {
            String response;

            //the game start
            response = in.readLine();
            if (response.startsWith("MESSAGE-START")) {
                System.out.println(response.substring(14));
            }

            //count
            for(int i = 0; i < 3; i++) {
                System.out.println(in.readLine());
            }

            //start
            System.out.println(in.readLine());

            for (int r = 0; r < 5; r++) {

                response = in.readLine();

                //question
                if (response.startsWith("QUESTION")) {
                    System.out.println(response.substring(9));
                    System.out.println(in.readLine());
                }

                while (true) {

                    response = in.readLine();
                    if (response.startsWith("MESSAGE-W-W")) {
                        //wrong ...
                        System.out.println(response.substring(12));
                        out.println(scanner.nextLine());
                    } else if (response.startsWith("MESSAGE-W-A")) {
                        System.out.println(response.substring(12));
                        for (int i = 0; i < 3; i++) {
                            System.out.println(in.readLine());
                        }
                        out.println(scanner.nextLine());
                    } else if (response.startsWith("MESSAGE-RESULT")) {
                        System.out.println(response.substring(15));
                    } else if (response.startsWith("MESSAGE-NEXT")) {
                        System.out.println(response.substring(13));
                        break;
                    } else if (response.startsWith("QUIT")) {
                        System.out.println(response.substring(5));
                        break;
                    } else if (response.startsWith("MESSAGE-WIN")) {
                        System.out.println(response.substring(12));
                    } else if (response.startsWith("MESSAGE-LOSE")) {
                        System.out.println(response.substring(13));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        while (true) {

            FibXLClient fibXLClient = new FibXLClient("localhost");
            fibXLClient.play();

            if (!fibXLClient.wantToPlayAgain()) {
                break;
            }

        }

    }

    private boolean wantToPlayAgain() {
        System.out.println("Want to play again?");
        return (Objects.equals(scanner.nextLine().toLowerCase(), "yes"));
    }

}
