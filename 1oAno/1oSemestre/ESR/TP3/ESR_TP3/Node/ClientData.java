package Node;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import Exceptions.BootstrapperException;
import Exceptions.NoRouteException;
import Protocol.Packet;

public class ClientData extends Node {
    private boolean hasRoute = false;
    private ReentrantLock streamLock;
    public Map<Integer, Packet> streamingData;
    public int currentImage = -1;
    public int imageCounter = 0;
    public final int maxCounter = 20;
    public boolean running = false;
    long startTime;

    public ClientData(String[] args) throws UnknownHostException, SocketException {
        super(args);
        streamingData = new HashMap<>();
        streamLock = new ReentrantLock();
    }

    // Resposta a cada pacote recebido
    public void handleRcvMessages(int messageType, Packet entry) throws IOException, BootstrapperException {
        switch (messageType) {
            case 0: // Inicio de conexao
                startConnection(entry);
                generateRoute();
                break;
            case 2: // Mensagem de ativacao da rota
                hasRoute = true;
                System.out.println("New route generated!");
                break;
            case 3: // Rececao de dados multimedia
                receiveFrames(entry);
                break;
            case 4: // Conexao de um novo vizinho
                System.out.println("New neighbour connected: " + entry.source);
                protocol.enableNeighbourInterface(entry.source);
                generateRoute();
                break;
            case 5: // Ping
                System.out.println(new String(entry.data) + " from " + entry.source);
                long elapsedTimeMillis = System.currentTimeMillis()-startTime;
                System.out.println("Ping: " + elapsedTimeMillis + " ms");
                break;
            case -1:
                nodeEndConnection(entry);
                break;
            default:
                break;
        }
    }

    // Rececao de dados multimedia
    public void receiveFrames(Packet entry) {
        streamLock.lock();
        try {
            if (running) {
                if (currentImage == -1)
                    currentImage = entry.fileInfo.seqNumber;
                streamingData.put(entry.fileInfo.seqNumber,entry);
            }
            else {
                if (currentImage != -1)
                    currentImage = -1;
                if (!streamingData.isEmpty()) {
                    streamingData.clear();
                }
            }
        }
        finally {
            streamLock.unlock();
        }
    
    }

    public Packet getStreamingPacket() {
        Packet p = null;
        streamLock.lock();
        try {
            if (running && !streamingData.isEmpty()) {
                p = streamingData.get(currentImage);
                streamingData.remove(currentImage);
                currentImage++;
                imageCounter++;
                if (imageCounter >= maxCounter) {
                    currentImage = -1;
                    imageCounter = 0;
                }
            }
        }
        finally {
            streamLock.unlock();
        }
        return p;
    }

    public void updateVideo(int metadataType) throws IOException, NoRouteException {
        if (hasRoute) {
            if (metadataType == 0) {
                if (!running) {
                    Packet p = new Packet(nodeId, bootstrapper.idNode, metadataType);
                    streamLock.lock();
                    protocol.sendAllNeighbours(p);
                    currentImage = -1;
                    running = true;
                    streamLock.unlock();
                }
            }
            else {
                if (running) {
                    Packet p = new Packet(nodeId, bootstrapper.idNode, metadataType);
                    protocol.sendAllNeighbours(p);
                    streamLock.lock();
                    running = false;
                    imageCounter = 0;
                    if (!streamingData.isEmpty()) {
                        streamingData.clear();
                    }
                    streamLock.unlock();
                }
            }
        }
        else {
            throw new NoRouteException(bootstrapper.idNode);
        }
    }

    // Resposta ao fim de conexao de um nodo da rede
    public void nodeEndConnection(Packet p) throws IOException, BootstrapperException {
        if (p.source.equals(bootstrapper.idNode)) {
            System.out.println("Bootstrapper ended connection!");
            throw new BootstrapperException();
        }
        else {
            System.out.println(p.source + " ended connection!");
            protocol.disableNeighbourInterface(p.source);
            hasRoute = false;
        }
    }

    // Calcula o caminho mais rapido do nodo ao servidor
    public void generateRoute() throws IOException {
        Packet p = new Packet(1, this.nodeId, bootstrapper.idNode, 0);
        protocol.sendAllNeighbours(p);
        System.out.println("Generating new route...");
    }

    // Mensagem de ping para o bootstrapper
    public void pingMessage() throws IOException {
        startTime = System.currentTimeMillis();
        Packet p = new Packet(5, nodeId, bootstrapper.idNode);
        protocol.sendAllNeighbours(p);
    }

    public void endConnection() throws IOException {
        Packet p = new Packet(-1, nodeId, bootstrapper.idNode);
        protocol.sendAllNeighbours(p);
    }
}
