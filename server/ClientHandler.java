package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.Message;

public class ClientHandler extends Thread {
    private final int id;
    private boolean connected;
    private final MainServer server;
    private String username;
    private final String userAddress;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    ClientHandler(MainServer server, Socket listener, int id) throws IOException {
        this.server = server;
        this.id = id;
        this.connected = true;
        this.username = String.valueOf(id);
        this.userAddress = listener.getRemoteSocketAddress().toString();
        this.outputStream = new ObjectOutputStream(listener.getOutputStream());
        this.inputStream = new ObjectInputStream(listener.getInputStream());
        System.out.println("The thread " + id + " is now connected with the player ip " + userAddress);
        this.start();
    }

    public synchronized void run() {
        Message message;

        try {

            //start listening for requests from client
            while (isConnected()) {
                message = read();

                switch (message.type){
                    case CONNECTION_REQUEST -> this.username = message.author;
                    case CHAT_MESSAGE -> {
                        if(message.author.equals(username)) {
                            server.sendAll(message);
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            connected = false;
            System.out.println(username + " disconnected");
        }
    }

    /**
     * Reads a serialized object received from the client.
     *
     * @return the object read.
     */
    private Message read() throws IOException, ClassNotFoundException {
        Message msg;

        if (this.isConnected()) {
            msg = (Message) inputStream.readObject();
            System.out.println("Message received by " + userAddress + "(" + username + ")");
        } else {
            throw new IOException();
        }

        return msg;
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param msg the {@code Message} we want to send to the client.
     */
    void send(Message msg) throws IOException {
        if (this.isConnected()) {
            this.outputStream.writeObject(msg);
            System.out.println("Message sent to " + userAddress + "(" + username + ")");
        } else {
            throw new IOException();
        }
    }

    boolean isConnected(){
        return connected;
    }

    String getAddress() {
        return userAddress;
    }

    String getUsername() {
        return this.username;
    }
}