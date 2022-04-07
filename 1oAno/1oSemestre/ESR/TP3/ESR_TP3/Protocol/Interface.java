package Protocol;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Interface {
    public String idNode;
    public InetAddress ipAdress;
    public boolean state; /* true -> active
                             false -> inactive */

    public Interface(DataInputStream dataIn) throws UnknownHostException, IOException{
        fromBytes(dataIn);
    }

    public Interface(String id,InetAddress ip) {
        this.idNode = id;
        this.ipAdress = ip;
        this.state = false;
    }
    public Interface(String id,InetAddress ip, boolean state) {
        this.idNode = id;
        this.ipAdress = ip;
        this.state = state;
    }

    public void toBytes(DataOutputStream dataOut) throws IOException {
        dataOut.writeUTF(idNode);
        dataOut.writeUTF(ipAdress.getHostAddress());
        dataOut.writeBoolean(state);
    }

    public void fromBytes(DataInputStream dataIn) throws UnknownHostException, IOException {
        idNode = dataIn.readUTF();
        ipAdress = InetAddress.getByName(dataIn.readUTF());
        state = dataIn.readBoolean();
    }


    @Override
    public String toString() {
        return "{" + idNode + " -> " + idNode.toString() + "; " + state + "}";
    }

}