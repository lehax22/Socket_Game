package test_model_Server_Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Alex on 16.12.2016.
 */
public class Client {

    private static BufferedReader in;
    private static PrintWriter out;
    private static Socket socket;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", 1234);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            System.out.println("Please, enter the your name");
            out.println(scanner.nextLine());

            Resender resender = new Resender();
            resender.start();

            String str = "";
            while (!str.equals("exit")) {
                str = scanner.next();
                out.println(str);
            }

            resender.setStop();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private static void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Created by Alex on 16.12.2016.
     */
    private static class Resender extends Thread{

        private boolean stop;

        public void setStop() {
            stop = true;
        }

        @Override
        public void run() {
            try {
                while (!stop) {
                    String str = in.readLine();
                    System.out.println(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
