package client;

import messages.Message;
import messages.MessageType;

import java.io.*;
import java.net.Socket;

public class NetworkHandler {
    private boolean connected;
    String username;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public NetworkHandler(String serverIp) {
        connect(serverIp);

        new InputHandler(this);
        clientStart();
    }

    private void clientStart() {
        Message message;

        try {

            //start listening for requests from client

            while (isConnected()) {
                message = read();

                if (message.type == MessageType.CHAT_MESSAGE && !message.author.equals(username)) {
                    System.out.println(message.author + ":\n" + message.message);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            connected = false;
            System.out.println(username + " disconnected");
        }
    }

    /**
     * Creates a connection between client and server, keeps trying until success.
     */
    private void connect(String serverIp) {
        boolean firstTime = true;

        while (!isConnected()) {
            try {
                Socket socket = new Socket(serverIp, 6969);
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                outputStream = new ObjectOutputStream(output);
                inputStream = new ObjectInputStream(input);
                connected = true;
                System.out.println("Connection Established!");
            } catch (IOException e) {
                if (firstTime) {
                    System.out.println("Cannot connect to the server, keep trying...");
                    firstTime = false;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException i) {
                    System.out.println("InterruptedException occurred!");
                }
            }
        }
    }


    /**
     * Reads a serialized object received from the client.
     *
     * @return the object read.
     */
    public Message read() throws IOException, ClassNotFoundException {
        messages.Message msg;

        if (this.isConnected()) {
            msg = (Message) inputStream.readObject();
        } else {
            throw new IOException();
        }

        return msg;
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param msg the object we want to send to the client.
     */
     void send(Message msg) throws IOException {
         if (isConnected()) {
             this.outputStream.writeObject(msg);
         } else {
             throw new IOException();
         }
     }

    synchronized boolean isConnected(){
        return connected;
    }

    void setUsername(String username) {
        this.username = username;
        try {
            this.send(new Message(MessageType.CONNECTION_REQUEST, username, ""));
        } catch (IOException e) {
            System.out.println("Connection with server lost");
        }
    }

    void sendMessage(String msg) {
        try {
            this.send(new Message(MessageType.CHAT_MESSAGE, this.username, msg));
        } catch (IOException e) {
            System.out.println("Connection with server lost");
        }
    }
}
