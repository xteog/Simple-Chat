
import client.NetworkHandler;

import java.io.IOException;


public class ClientApp {
    private static String serverIp = "127.0.0.1";

    public static void main(String[] args) {
        new NetworkHandler(serverIp);
    }
}
