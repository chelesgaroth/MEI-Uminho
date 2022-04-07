package Protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;

public class Packet {
    public int messageType; /*
                             * -1 -> End connection
                             * 0 -> Start connection
                             * 1 -> Routing message
                             * 2 -> Routing activation
                             * 3 -> Streaming multimedia
                             * 4 -> Neighbours activation
                             * 5 -> Ping
                             */
    public Metadata fileInfo;
    public String source; // Nome do primeiro nodo a enviar a mensagem
    public String destination; // Nome do nodo destino
    public int nHops; // Numero de saltos (usado no pacote de routing)
    public InetAddress prevNodeIp; // Obtido a partir do pacote udp (não é enviado no payload UDP)
    public byte[] data; // Header do pacote inclui o tamanho do payload (em bytes)

    //Pacote streaming pedidos
    public Packet(String source, String destination, int metadataMessageType) {
        this.messageType = fileInfo();
        this.source = source;
        this.destination = destination;
        this.fileInfo = new Metadata(metadataMessageType);
        this.data = null;
    }

    // Pacote streaming de envio de dados
    public Packet(String source, int metadataSeqNumber, Set<String> destinations,byte[] data) {
        this.messageType = fileInfo();
        this.source = source;
        this.destination = null;
        this.fileInfo = new Metadata(3, metadataSeqNumber, destinations);
        this.data = data;
    }

    public Packet(int messageType, String source, String destination) {
        this.messageType = messageType;
        this.source = source;
        this.destination = destination;
        this.fileInfo = null;
        this.data = null;
    }

    public Packet(int messageType, String source, String destination, int nhops) {
        this.messageType = messageType;
        this.source = source;
        this.destination = destination;
        this.nHops = nhops;
        this.fileInfo = null;
        this.data = null;
    }

    public Packet(int messageType, String source, String destination, byte[] data) {
        this.messageType = messageType;
        this.source = source;
        this.destination = destination;
        this.fileInfo = null;
        this.data = data;
    }

    public Packet(InetAddress ipNode, byte[] data) throws IOException {
        bytesToPacket(ipNode, data);
    }

    // Retorna o id do tipo de mensagem que precisa de ter o atributo 'fileInfo'
    public int fileInfo() {
        return 3;
    }

    public byte[] packetToBytes() throws IOException {
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        final DataOutputStream dataOut = new DataOutputStream(byteOut);
        dataOut.writeInt(messageType);
        if (fileInfo != null) {
            fileInfo.toBytes(dataOut);
        }
        dataOut.writeUTF(source);
        if (destination != null)
            dataOut.writeUTF(destination);

        if (messageType == 1 || messageType == 2) {
            dataOut.writeInt(nHops);
        }
        if (data != null) {
            dataOut.writeInt(data.length);
            dataOut.write(data);
        } else {
            dataOut.writeInt(0);
        }
        dataOut.close();
        byteOut.flush();
        return byteOut.toByteArray();
    }

    public void bytesToPacket(InetAddress ipNode, byte[] packetBytes) throws IOException {
        final ByteArrayInputStream byteIn = new ByteArrayInputStream(packetBytes);
        final DataInputStream dataIn = new DataInputStream(byteIn);
        messageType = dataIn.readInt();
        if (messageType == fileInfo())
            fileInfo = new Metadata(dataIn);
        else
            fileInfo = null;
        source = dataIn.readUTF();
        if (fileInfo == null) {
            destination = dataIn.readUTF();
        }
        else if (fileInfo.messageType != 3) {
            destination = dataIn.readUTF();
        }

        if (messageType == 1 || messageType == 2) {
            nHops = dataIn.readInt();
        }
       
        int dataBytes = dataIn.readInt();
        if (dataBytes > 0) {
            data = dataIn.readNBytes(dataBytes);
        } else
            data = null;
        dataIn.close();
        byteIn.close();
        prevNodeIp = ipNode;
    }

    @Override
    public String toString() {
        return "{" +
                " messageType='" + messageType + "'" +
                ", source='" + source + "'" +
                ", destination='" + destination + "'" +
                ", prevNodeIp='" + prevNodeIp.getHostAddress() + "'" +
                ", fileInfo='" + fileInfo + "'" +
                ", data='" + data + "'" +
                "}";
    }

}
