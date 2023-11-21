package server;

import messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;

public class MainServer {
    private int threadCount = 0;
    private final ArrayList<ClientHandler> clients = new ArrayList<>();


    public MainServer() throws IOException, InterruptedException, AlreadyBoundException {
        Socket socket;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(6969);
        } catch (IOException e) {
            System.out.println("Failed in creating a socket.");
        }

        System.out.println("Server online");

        if (serverSocket != null) {
            boolean running = true;
            while (running) {
                try {
                    socket = serverSocket.accept();

                    ClientHandler client = new ClientHandler(this, socket, getThreadCount());

                    clients.add(client);

                } catch (IOException e) {
                    System.out.println("Can't connect with client");
                    running = false;
                }
            }
        }
    }


    /**
     * Method that sends to all the users a message.
     *
     * @param msg The message to send.
     */
    void sendAll(Message msg) throws IOException {
        for (ClientHandler client : clients){
            client.send(msg);
        }
    }

    private synchronized int getThreadCount() {
        return threadCount++;
    }
}
