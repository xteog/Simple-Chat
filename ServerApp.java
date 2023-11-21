import java.io.IOException;
import java.rmi.AlreadyBoundException;

import server.MainServer;

public class ServerApp {
    public static void main(String[] args) {
        try {
            new MainServer();
        } catch (IOException | InterruptedException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}