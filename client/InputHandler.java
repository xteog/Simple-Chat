package client;

import messages.Message;
import messages.MessageType;

import java.util.Scanner;

public class InputHandler extends Thread{
    private final NetworkHandler networkHandler;

    InputHandler(NetworkHandler networkHandler){
        this.networkHandler = networkHandler;
        this.start();
    }

    public synchronized void run(){
        String input;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Insert Username: ");

        input = scanner.nextLine();
        networkHandler.setUsername(input);

        while(networkHandler.isConnected()) {
            input = scanner.nextLine();
            networkHandler.sendMessage(input);
        }
    }
}
