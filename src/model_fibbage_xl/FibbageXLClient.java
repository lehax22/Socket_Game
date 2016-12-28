package model_fibbage_xl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Alex on 18.12.2016.
 */
public class FibbageXLClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final int PORT = 1234;

    public FibbageXLClient(String localhost) throws IOException {
        socket = new Socket(localhost, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        String response = in.readLine();
        if (response.startsWith("WELCOME")) {
            System.out.println(response);
            out.println(new Scanner(System.in).nextLine());
        }

        System.out.println(in.readLine().substring(8));
    }

    private void play() throws IOException {
        String response;

        System.out.println(in.readLine().substring(14));

        while (true) {
            response = in.readLine();
            if (response.startsWith("QUESTION")) {
                System.out.println(response.substring(9));
            } else if (response.startsWith("MESSAGE-ENTER")) {
                System.out.println(response.substring(14));
                out.println(new Scanner(System.in).nextLine());
            } else if (response.startsWith("WAY"))  {
                for (int i = 0; i < 3; i++) {
                    System.out.println(in.readLine());
                }
            } else if (response.startsWith("MESSAGE-SELECT")) {
                System.out.println(response.substring(14));
                out.println(new Scanner(System.in).nextLine());
            } else if (response.startsWith("RESULT")) {
                System.out.println(response.substring(7));
                System.out.println(in.readLine());
            }
        }
    }

    private boolean wantToPlayAgain() {
        System.out.println("Want to play again? YES or NO");
        return ("yes" == new Scanner(System.in).next().toLowerCase());
    }

    public static void main(String[] args) throws IOException {
        while (true) {
            FibbageXLClient fibbageXLClient = new FibbageXLClient("localhost");
            fibbageXLClient.play();
            if (!fibbageXLClient.wantToPlayAgain()) {
                break;
            }
        }
    }

}
