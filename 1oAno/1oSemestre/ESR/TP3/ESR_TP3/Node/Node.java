package Node;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import Protocol.Interface;
import Protocol.Packet;
import Protocol.PacketProtocol;

public abstract class Node {
    public Interface bootstrapper;
    public String nodeId;
    public PacketProtocol protocol;
    

    public Node(String[] args) throws UnknownHostException, SocketException {
        this.nodeId = args[0];
        this.bootstrapper = new Interface(args[1],InetAddress.getByName(args[2]));
        this.protocol = new PacketProtocol();
        
    }

    // Thread principal que inicia conexao e responde a mensagens recebidas
    public void start() throws Exception {
        System.out.println("Starting connection with bootstrapper. Waiting for neighbour list...");
        protocol.startNodeConnection(nodeId, this.bootstrapper);
        protocol.startRcv();
        while(true) {
            Packet entry = protocol.receive();
            handleRcvMessages(entry.messageType, entry);
        }
    }

    public abstract void handleRcvMessages(int messageType, Packet entry) throws Exception;

    // Resposta a mensagem de inicio de conexao
    public void startConnection(Packet p) throws IOException {
        protocol.updateNeighbours(p.data);
        protocol.enableNeighbourInterfaces(nodeId);
        System.out.println("Connection established!");
        System.out.println("Neighbour list:");
        System.out.println(protocol.neighbours.toString());
    }


}