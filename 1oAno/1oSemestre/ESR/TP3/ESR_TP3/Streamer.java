import java.net.*;

import Streamer.StreamerData;

// Nó que vai enviar dados para os clientes
public class Streamer {
    private static StreamerData streamer;
    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Ending connection...");
                streamer.endConnection();
            }
        });

        try {
            streamer = new StreamerData(args);
            streamer.start();
            
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            System.exit(0);
            
        }
    }
    
}