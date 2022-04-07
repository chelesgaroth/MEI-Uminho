package Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// Header adicional para a pacote multimedia
public class Metadata {
    public int messageType; /*
                             * 0 -> Start video
                             * 1 -> Pause video
                             * 2 -> Stop video
                             * 3 -> Stream video
                             */
    // String filename;
    // long fileSize;
    public int seqNumber;
    public Set<String> destinations;

    public void toBytes(DataOutputStream dataOut) throws IOException {
        dataOut.writeInt(messageType);
        if (messageType == 3) {
            dataOut.writeInt(seqNumber);
            dataOut.writeInt(destinations.size());
            for (String d : destinations) {
                dataOut.writeUTF(d);
            }
        }

    }

    public Metadata(int messageType) {
        this.messageType = messageType;
    }

    public Metadata(int messageType, int packetNumber) {
        this.messageType = messageType;
        this.seqNumber = packetNumber;
    }

    public Metadata(int messageType, int packetNumber, Set<String> dests) {
        this.messageType = messageType;
        this.seqNumber = packetNumber;
        this.destinations = dests;
    }

    public Metadata(DataInputStream dataIn) throws IOException {
        messageType = dataIn.readInt();
        if (messageType == 3) {
            seqNumber = dataIn.readInt();
            int length = dataIn.readInt();
            destinations = new HashSet<>();
            for (int i = 0; i < length; i++) {
                String s = dataIn.readUTF();
                destinations.add(s);
            }
        }
    }


    @Override
    public String toString() {
        String d = "null";
        if (destinations != null) d = destinations.toString();
        return "{" +
            " messageType='" + messageType + "'" +
            ", seqNumber='" + seqNumber + "'" +
            ", destinations='" + d + "'" +
            "}";
    }
    

}
