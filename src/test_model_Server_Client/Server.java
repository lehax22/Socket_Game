package test_model_Server_Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alex on 16.12.2016.
 */
public class Server {

    public static List<Connection> connectionList = new ArrayList<>();
    public static ServerSocket serverSocket;

    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(1234);
            while (true) {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                connectionList.add(connection);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }

    }

    private static void closeAll() {
        try {
            serverSocket.close();

            synchronized (connectionList) {
                Iterator<Connection> iter = connectionList.iterator();
                while (iter.hasNext()) {
                    ((Connection)iter.next()).close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Created by Alex on 16.12.2016.
     */
    public static class Connection extends Thread{

        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        String name = "";

        public Connection(Socket socket) {
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                name = in.readLine();
                synchronized (connectionList) {
                    Iterator<Connection> iter = connectionList.iterator();
                    while (iter.hasNext()) {
                        ((Connection)iter.next()).out.println(name + " cames now");
                    }
                }

                String str = "";
                while (true) {
                    str = in.readLine();
                    if (str.equals("exit")) break;

                    synchronized (connectionList) {
                        Iterator<Connection> iter = connectionList.iterator();
                        while (iter.hasNext()) {
                            ((Connection) iter.next()).out.println(name + " has left");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        private void close() {
            try {
                in.close();
                out.close();
                socket.close();

                connectionList.remove(this);
                if (connectionList.size() == 0) {
                    /*Server.this.closeAll();*/
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
