package Protocol;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PacketProtocol {
    private ReentrantLock sendLock; // lock para socket de envio de pacotes
    private DatagramSocket sendSocket; // socket para enviar pacotes
    private DatagramSocket rcvSocket; // socket para receber pacotes
    private ArrayDeque<Packet> messages; // lista de pacotes recebidos e o ip do ultimo nodo que os enviou
    private ReentrantLock lock; // lock para acesso a fila 'messages'
    private Condition cond; // condition para acesso a fila 'messages'
    public Map<String,Interface> neighbours; // Map de vizinhos de um nó



    public PacketProtocol() throws SocketException {
        this.sendLock = new ReentrantLock();
        this.sendSocket = new DatagramSocket();
        this.rcvSocket = new DatagramSocket(9090);
        this.neighbours = null;
        this.messages = new ArrayDeque<>();
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
    }

    // Construtor usado pelo streamer que ja sabe os seus vizinhos
    public PacketProtocol(List<Interface> neighbours) throws SocketException {
        this.sendLock = new ReentrantLock();
        this.sendSocket = new DatagramSocket();
        this.rcvSocket = new DatagramSocket(9090);
        this.messages = new ArrayDeque<>();
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        updateNeighbours(neighbours);
    }

    // Store received messages in a queue
    public void startRcv() {
        (new Thread() {
            @Override
            public void run() {
                DatagramPacket packet = new DatagramPacket(new byte[65535], 65535);
                
                while (true) {
                    try {
                        rcvSocket.receive(packet);
                        byte[] packetData = packet.getData();
                        Packet p = new Packet(packet.getAddress(),packetData);
                        //System.out.println("Message received from " + p.source);
                        try {
                            lock.lock();
                            messages.push(p);
                        }
                        finally {
                            cond.signalAll();
                            lock.unlock();
                        }
                    } 
                    catch (IOException ex) {
                        rcvSocket.close();
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    public Packet receive() {
        lock.lock();
        while (messages.isEmpty()) {
            try {
                cond.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        lock.lock();
        Packet p = messages.poll();
        lock.unlock();
        return p;
    }

    public void send(Packet p, String dest) throws IOException {
        byte[] data = p.packetToBytes();
        DatagramPacket dp = new DatagramPacket(data,data.length,neighbours.get(dest).ipAdress, 9090);
        sendLock.lock();
        try {
            sendSocket.send(dp);
        }
        finally {
            sendLock.unlock();
        }
    }

    public void send(Packet p, InetAddress dest) throws IOException {
        byte[] data = p.packetToBytes();
        DatagramPacket dp = new DatagramPacket(data,data.length,dest, 9090);
        sendLock.lock();
        try {
            sendSocket.send(dp);
        }
        finally {
            sendLock.unlock();
        }
    }

    public void sendAllNeighbours(Packet p) {
        byte[] data;
        try {
            data = p.packetToBytes();
            for(Interface i : neighbours.values()) {
                if (i.state) {
                    DatagramPacket dp = new DatagramPacket(data,data.length,i.ipAdress, 9090);
                    sendLock.lock();
                    try {
                        sendSocket.send(dp);
                    }
                    finally {
                        sendLock.unlock();
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void sendAllNeighboursExcept(Packet p, InetAddress ip) throws IOException {
        byte[] data = p.packetToBytes();
        for(Interface i : neighbours.values()) {
            if (i.state && !ip.equals(i.ipAdress)) {
                DatagramPacket dp = new DatagramPacket(data,data.length,i.ipAdress, 9090);
                sendLock.lock();
                try {
                    sendSocket.send(dp);
                }
                finally {
                    sendLock.unlock();
                }
            }
        }
    }

    public void startNodeConnection(String nodeId, Interface bootstrapper) throws IOException {
        Packet p = new Packet(0, nodeId, bootstrapper.idNode);
        send(p, bootstrapper.ipAdress);
    }

    // Mensagem a avisar os vizinhos que se conectou
    public void enableNeighbourInterfaces(String idNode) throws IOException {
        for (Interface i : neighbours.values()) {
            if (i.state == true) {
                Packet p = new Packet(4, idNode, i.idNode);
                send(p, i.idNode);
            }
        }
    }

    // Ativar interface de um vizinho
    public void enableNeighbourInterface(String idNode) throws IOException {
        Interface i = neighbours.get(idNode);
        if (i != null)
            i.state = true;
    }

    // Desativar interface de um vizinho
    public void disableNeighbourInterface(String idNode) throws IOException {
        Interface i = neighbours.get(idNode);
        if(i != null)
            i.state = false;
    }

    public void updateNeighbours(byte[] data) throws IOException {
        neighbours = new HashMap<>();
        final ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
        final DataInputStream dataIn = new DataInputStream(byteIn);
        int length = dataIn.readInt();
        for (int i = 0; i < length; i++) {
            Interface interf = new Interface(dataIn);
            neighbours.put(interf.idNode, interf);
        }
        dataIn.close();
        byteIn.close();
    }

    public void updateNeighbours(List<Interface> list) {
        neighbours = new HashMap<>();
        for (Interface i : list) {
            neighbours.put(i.idNode, i);
        }
    }

    public boolean isNeighbour(String idNode) {
        return neighbours.containsKey(idNode);
    }

    public String getNeighbourIdNode(InetAddress ip) {
        String node = null;
        for (Interface i : neighbours.values()) { 
            if (i.ipAdress.equals(ip)) {
                node = i.idNode;
                break;
            }
        }
        return node;
    }

    public boolean isRouter(String idnode) {
        return idnode.charAt(0) == 'O';
    }

    public List<InetAddress> getRouterNeighbours() {
        List<InetAddress> routers = new ArrayList<>();
        for(Interface node : neighbours.values()) {
            if (isRouter(node.idNode) && node.state == true)
                routers.add(neighbours.get(node.idNode).ipAdress);
        }
        return routers;
    }
}
