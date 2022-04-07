package Streamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Timer;

import Protocol.Interface;
import Protocol.Packet;
import Protocol.PacketProtocol;

public class StreamerData implements ActionListener {
    private String nodeId; // Nome do nodo
    private PacketProtocol protocol;
    private final String filePath = "Streamer/";
    private Map<String, List<Interface>> overlay;
    private Set<String> clientStreaming;
    private VideoStream stream;
    private Timer sTimer;
    static int FRAME_PERIOD = 20;

    // Video
    String videoName;

    public StreamerData(String[] args) throws Exception {
        nodeId = args[0];
        this.overlay = new HashMap<>();
        
        parse(filePath + args[1]);
        this.protocol = new PacketProtocol(overlay.get(nodeId));
        if (args.length >= 3) {
            videoName = args[2];
        } else {
            videoName = "movie.Mjpeg";
        }
        System.out.println("Video fileName: " + videoName);
        this.stream = new VideoStream(videoName);
        clientStreaming = new HashSet<>();
        sTimer = new Timer(FRAME_PERIOD, this); // init Timer para servidor
        sTimer.setInitialDelay(0);
        sTimer.setCoalesce(true);
    }

    public void start() throws Exception {
        sTimer.start();
        protocol.startRcv();
        while (true) {
            Packet p = protocol.receive();
            handleRcvMessages(p.messageType, p);
        }
    }

    

    // Resposta a cada pacote recebido
    public void handleRcvMessages(int messageType, Packet p) throws Exception {
        switch (messageType) {
            case 0: // Inicio de conexao
                startConnection(p);
                activateNeighbourInterfaces(p.source);
                break;
            case 1: // Mensagem de routing
                setNewRoute(p);
                break;
            case 2: // Mensagem de ativacao da rota
                break;
            case 3: // Pedido de envio de multimedia
                handleMultimediaMessages(p);
                break;
            case 4: 
                protocol.enableNeighbourInterface(p.source);
                System.out.println("New neighbour connected: " + p.source);
                break;
            case 5: // Mensagem de ping
                pingMessage(p);
                break;
            case -1: // Fim de conexao
                disableNeighbourInterfaces(p.source);
                clientStreaming.remove(p.source);
                break;
            default:
                break;
        }
    }

    
    public void handleMultimediaMessages(Packet p) throws Exception {
        switch (p.fileInfo.messageType) {
            case 0: // play
                System.out.println("Start streaming to "+ p.source);
                clientStreaming.add(p.source);
                break;
            case 1: // pause
                System.out.println("Stop streaming to "+ p.source);
                clientStreaming.remove(p.source);
                break;
            default:
                break;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        byte[] data;
        try {
            data = stream.actionPerformed();
            if(!clientStreaming.isEmpty()) {
                Packet p = new Packet(nodeId, stream.imagenb, clientStreaming, data);
                protocol.sendAllNeighbours(p);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void pingMessage(Packet p) throws IOException, InterruptedException {
        byte[] data = "Hello!".getBytes();
        Packet ping = new Packet(5, nodeId, p.source, data);
        protocol.sendAllNeighbours(ping);
    }

    // Resposta a mensagem de inicio de conexao
    // Comunicar ao nodo que se conecta os seus vizinhos
    public void startConnection(Packet p) throws IOException {
        List<Interface> neighList = overlay.get(p.source);
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        final DataOutputStream dataOut = new DataOutputStream(byteOut);
        dataOut.writeInt(neighList.size());
        for (Interface i : neighList) {
            i.toBytes(dataOut);
        }
        dataOut.close();
        byteOut.flush();
        Packet pSend = new Packet(0, nodeId, p.source, byteOut.toByteArray());
        protocol.send(pSend, p.prevNodeIp);
    }


    public void activateNeighbourInterfaces(String idnode) {
        for (List<Interface> li : overlay.values()) {
            for (Interface i : li) {
                if (i.idNode.equals(idnode))
                    i.state = true;
            }
        }
    }

    public void disableNeighbourInterfaces(String idnode) {
        System.out.println("Node ended connection: " + idnode);
        for (List<Interface> li : overlay.values()) {
            for (Interface i : li) {
                if (i.idNode.equals(idnode))
                    i.state = false;
            }
        }
    }

    // Resposta a mensagem de routing
    // Ativar a rota
    public void setNewRoute(Packet p) throws IOException {
        Packet rt = new Packet(2, nodeId, p.source, 0);
        protocol.sendAllNeighbours(rt);
        System.out.println("Activating route for " + p.source);
    }

    public void endConnection()  {
        Packet p = new Packet(-1, nodeId, "");
        protocol.sendAllNeighbours(p);
       
    }

    // Parse do ficheiro de configuracao
    public void parse(String filename) throws IOException {

        // this method of try with resorces automatically closes buffered reader
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line = reader.readLine();

            while (line != null) {

                String[] parts = line.split(" *: *");
                String[] ips = parts[1].split(" *; *");

                List<Interface> interfaceList = new ArrayList<>();

                // loop through all neighbours of a node
                for (String s : ips) {
                    String[] data = s.split(" *, *");
                    InetAddress ina = InetAddress.getByName(data[1]);
                    Interface i;
                    if (data[0].equals(nodeId))
                        i = new Interface(data[0], ina, true);
                    else
                        i = new Interface(data[0], ina, false);
                    interfaceList.add(i);
                }

                this.overlay.put(parts[0], interfaceList);

                // read next line
                line = reader.readLine();
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

}