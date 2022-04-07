package Node;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Exceptions.BootstrapperException;
import Exceptions.NoRouteException;
import Protocol.Interface;
import Protocol.Packet;

public class RouterData extends Node {
    private Map<String, Map<InetAddress, TableEntry>> routingTable;

    public RouterData(String[] args) throws UnknownHostException, SocketException {
        super(args);

    }

    // Resposta a cada pacote recebido
    public void handleRcvMessages(int messageType, Packet entry) throws Exception {
        switch (messageType) {
            case 0: // Inicio de conexao
                startConnection(entry);
                updateInitialRoutingTable();
                break;
            case 1: // Mensagem de routing
                updateRoutingTable(entry);
                spreadRoutingPacket(entry);
                break;
            case 2: // Mensagem de ativacao da rota
                activateNewInterfaces(entry);
                break;
            case 3: // Stream multimedia
                handleMultimediaMessages(entry);
                break;
            case 4: // Ativacao de um vizinho
                protocol.enableNeighbourInterface(entry.source);
                System.out.println("New neighbour connected: " + entry.source);
                break;
            case 5: // Ping message
                send(entry);
                break;
            case -1:
                nodeEndConnection(entry);
                System.out.println("Node ended connection: " + entry.source);
                break;
            default:
                break;
        }
    }


    public void handleMultimediaMessages(Packet p) throws Exception {
        if (p.fileInfo.messageType == 3) { // Se for pacote de streaming de dados
            Map<InetAddress,Set<String>> dests = new HashMap<>();
            for (String node : p.fileInfo.destinations) {
                InetAddress ip = getActiveRoute(node);
                if (ip != null) {
                    Set<String> list = dests.get(ip);
                    if (list == null) 
                        list = new HashSet<>();
                    list.add(node);
                    dests.put(ip, list);
                }
            }
            for (Map.Entry<InetAddress,Set<String>> e : dests.entrySet()) {
                Packet newP = new Packet(p.source, p.fileInfo.seqNumber, e.getValue(), p.data);
                protocol.send(newP, e.getKey());
                System.out.print("Packet -> NextHop: " + e.getKey() + ", Destinations: " + e.getValue().toString());
            }
        }
        else {
            send(p);
        }
    }

    // Resposta ao fim de conexao de um nodo da rede
    public void nodeEndConnection(Packet p) throws IOException, BootstrapperException {
        if (p.source.equals(bootstrapper.idNode)) {
            String prevNodeId = protocol.getNeighbourIdNode(p.prevNodeIp);
            protocol.disableNeighbourInterface(prevNodeId);
            protocol.sendAllNeighbours(p);
            throw new BootstrapperException();
        }
        else {
            try {
                send(p);
            } catch (NoRouteException e) {
                System.out.println(e.getMessage());
                System.out.println("Sending to all neighbours...");
                protocol.sendAllNeighboursExcept(p, p.prevNodeIp);
            }
            if (protocol.isNeighbour(p.source)) {
                disableNeighbour(p.source);
            }
            else {
                if (!protocol.isRouter(p.source)) {
                    for (TableEntry te : routingTable.get(p.source).values()) {
                        te.state = false;
                    }
                }
            }
        }

    }

    public void disableNeighbour(String idnode) throws IOException {
        protocol.disableNeighbourInterface(idnode);
        InetAddress ip = protocol.neighbours.get(idnode).ipAdress;
        for (String id : routingTable.keySet()) {
            TableEntry te = routingTable.get(id).get(ip);
            if (te != null)
                te.state = false;
        }
    }


    public void endConnection() {
        Packet p = new Packet(-1, nodeId, bootstrapper.idNode);
        protocol.sendAllNeighbours(p);
    }

    // Direcionar os pacotes recebidos para o proximo vizinho
    public void send(Packet p) throws IOException, NoRouteException {
        InetAddress dest = getActiveRoute(p.destination);
        protocol.send(p, dest);
        
    }

    public InetAddress getActiveRoute(String dest) throws NoRouteException {
        Map<InetAddress, TableEntry> entry = routingTable.get(dest);
        InetAddress address = null;
        if (entry != null) {
            for (TableEntry e : entry.values()) {
                if (e.state == true) {
                    address = e.nextHop;
                    break;
                }
            }
        }
        if (address == null) throw new NoRouteException(dest);
        return address;
    }

    public void updateInitialRoutingTable() throws UnknownHostException {
        this.routingTable = new HashMap<>();
        for (Interface i : protocol.neighbours.values()) {
            TableEntry e = new TableEntry(i.idNode, i.ipAdress, 1, i.state);
            Map<InetAddress, TableEntry> entry = new HashMap<>();
            entry.put(e.nextHop, e);
            routingTable.put(e.destination, entry);
        }
    }

    // Atualizar tabela de routing
    public void updateRoutingTable(Packet p) throws UnknownHostException {
        Map<InetAddress, TableEntry> entry = routingTable.get(p.source);
        if (entry == null) {
            entry = new HashMap<>();
            TableEntry e = new TableEntry(p.source, p.prevNodeIp, p.nHops + 1);
            entry.put(e.nextHop, e);
            routingTable.put(e.destination, entry);
        } else {
            TableEntry e = entry.get(p.prevNodeIp);
            if (e == null) {
                e = new TableEntry(p.source, p.prevNodeIp, p.nHops + 1);
                entry.put(e.nextHop, e);
            } else {
                if (p.nHops + 1 < e.cost) {
                    e.cost = p.nHops + 1;
                }
            }
        }
    }

    public TableEntry getLessCostTableEntry(String dest) {
        TableEntry minCost = null;
        Map<InetAddress, TableEntry> entries = routingTable.get(dest);
        for (TableEntry r : entries.values()) {
            if (minCost == null)
                minCost = r;
            else {
                if (r.cost < minCost.cost)
                    minCost = r;
            }
        }
        return minCost;
    }

    // Escolhe o router cujo caminho é mais perto
    public TableEntry getLessCostTableEntry(String dest, List<InetAddress> routers) {
        TableEntry minCost = null;
        for (InetAddress r : routers) {
            if (minCost == null)
                minCost = routingTable.get(dest).get(r);
            else {
                TableEntry entry = routingTable.get(dest).get(r);
                if (entry.cost < minCost.cost)
                    minCost = entry;
            }
        }
        return minCost;
    }

    public void spreadRoutingPacket(Packet p) throws IOException {
        List<InetAddress> routers = super.protocol.getRouterNeighbours();
        Set<InetAddress> entryRouters = new HashSet<>();
        if (routingTable.get(p.source) != null)
            entryRouters = routingTable.get(p.source).keySet(); // Routers de quem ja ha mensagem de routing
        if (super.protocol.isNeighbour(p.destination)) { // O destino é um dos seus vizinhos
            boolean receivedAll = true;
            for (InetAddress r : routers) {
                if (!entryRouters.contains(r)) {
                    receivedAll = false;
                    break;
                }
            }
            if (receivedAll) { // Se ja recebeu mensagem de routing de todos os vizinhos
                int minCost = getLessCostTableEntry(p.source, routers).cost;
                Packet rp = new Packet(1, p.source, p.destination, minCost);
                protocol.send(rp, p.destination); // Envia mensagem diretamente ao destino
            }
        } else { // O destino nao é um dos seus vizinhos
            for (InetAddress r : routers) {
                if (!entryRouters.contains(r)) {
                    int minCost = getLessCostTableEntry(p.source).cost;
                    Packet rp = new Packet(1, p.source, p.destination, minCost);
                    protocol.send(rp, r); // Envia aos routers vizinhos
                }
            }
        }
    }

    public void activateNewInterfaces(Packet p) throws IOException {
        Map<InetAddress, TableEntry> bootstrapperEntry = routingTable.get(bootstrapper.idNode);
        if (bootstrapperEntry == null) {
            bootstrapperEntry = new HashMap<>();
            TableEntry e = new TableEntry(bootstrapper.idNode, p.prevNodeIp, p.nHops + 1);
            e.activateInterface();
            bootstrapperEntry.put(e.nextHop, e);
            routingTable.put(bootstrapper.idNode, bootstrapperEntry);
        } else {
            TableEntry e;
            e = getLessCostTableEntry(bootstrapper.idNode);
            e.activateInterface();
            
        }
        TableEntry entry = getLessCostTableEntry(p.destination);
        entry.activateInterface();
        Packet newP = new Packet(2, p.source, p.destination, p.nHops + 1);
        protocol.send(newP, entry.nextHop);
        System.out.print("Entry enabled ->");
        System.out.println(p.destination + ": " + entry.nextHop.toString());
    }
}
