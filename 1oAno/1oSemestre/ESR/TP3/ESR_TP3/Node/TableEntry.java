package Node;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TableEntry {
    public String destination;
    public boolean state; // false - inactive; true - active;
    public int cost; // number of hops
    public InetAddress nextHop;
    

    public TableEntry(String dest, String next) throws UnknownHostException {
        this.destination = dest;
        this.nextHop = InetAddress.getByName(next);
        this.state = false;
        this.cost = 0;
    }

    public TableEntry(String dest, InetAddress next, int cost) throws UnknownHostException {
        this.destination = dest;
        this.nextHop = next;
        this.state = false;
        this.cost = cost;
    }

    public TableEntry(String dest, InetAddress next, int cost, boolean state) throws UnknownHostException {
        this.destination = dest;
        this.nextHop = next;
        this.state = state;
        this.cost = cost;
    }

    public void activateInterface() {
        state = true;
    }

    public void sumCost() {
        this.cost++;
    }

    public void compareNextHop(int newCost, InetAddress newNextHop) {
        if (newCost < cost) {
            cost = newCost;
            nextHop = newNextHop;
        }
    }


    @Override
    public String toString() {
        return "{" +
            " destination='" + destination + "'" +
            ", state='" + state + "'" +
            ", cost='" + cost + "'" +
            ", nextHop='" + nextHop + "'" +
            "}";
    }

}
