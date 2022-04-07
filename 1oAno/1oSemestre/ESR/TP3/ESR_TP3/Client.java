import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.Timer;

import Exceptions.NoRouteException;
import GUI.ClientGUI;

import Node.ClientData;
import Protocol.Packet;

// Nó que vai receber os dados enviados pelo streamer
public class Client {

    private static ClientGUI gui;
    private static ClientData client;
    private static Timer cTimer;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    System.out.println("Ending connection...");
                    cTimer.stop();
                    client.endConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            client = new ClientData(args);
            gui = new ClientGUI();
            cTimer = new Timer(20, new clientTimerListener());
            cTimer.setInitialDelay(0);
            cTimer.setCoalesce(true);
            addListeners();
            client.start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }

    // LISTENERS
    private static void addListeners() {
        gui.playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handlePlayPress();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        gui.pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handlePausePress();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        gui.setupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handlePingPress();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        gui.tearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleTearPress();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    // HANDLERS
    static class clientTimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // get an Image object from the payload bitstream
            if (!client.streamingData.isEmpty()) {
                Packet p = client.getStreamingPacket();
                if (p != null && p.data != null)
                    gui.displayVideo(p.data);
                    
            }
        }

    }

    private static void handlePlayPress() throws IOException {
        System.out.println("Play Button pressed !");
        // start the timers ...
        try {
            client.updateVideo(0);
            cTimer.start();
        } catch (NoRouteException e) {
            System.out.println(e.getMessage());
        }
        
        

    }

    private static void handlePausePress() throws IOException {
        System.out.println("Pause Button pressed !");
        // start the timers ...
        try {
            client.updateVideo(1);
            cTimer.stop();
        } catch (NoRouteException e) {
            System.out.println(e.getMessage());
        }
        

    }

    private static void handlePingPress() throws IOException {
        System.out.println("Ping Button pressed !");
        client.pingMessage();

    }

    private static void handleTearPress() throws IOException {
        System.out.println("Teardown Button pressed !");
        // exit
        System.exit(0);
    }
}