import gateway.beans.Node;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import messageproto.Message;
import messageproto.MessageChangeGrpc;
import nodo.*;
import simulatore.BufferImpl;
import simulatore.PM10Simulator;
import tokenproto.Token;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;


public class MainNode {
    private static final String URI_POST_STATISTIC = "http://localhost:6060/gatewayNode/addStatistics";

    private static HttpClient client;
    private static Node thisNode;
    private static NodeServer nodeServer;

    //Threads
    private static BufferImpl bufferimpl;
    private static PM10Simulator pm10Simulator;
    private static TokenManager tokenManager;

    //Lock
    private static Object lockAdd = new Object();

    private static List<Node> listNodes = new ArrayList<>();

    private static final Logger log = Logger.getLogger("global");

    public static void main(String[] args) throws Exception {

        Scanner in = new Scanner(System.in);
        client = HttpClient.newHttpClient();

        NodeAdded addNodo = new NodeAdded(client, lockAdd);
        addNodo.start();

        synchronized (lockAdd) {
            lockAdd.wait();
        }

        log.info("Added nodo");
        listNodes = addNodo.listNodes;
        thisNode = addNodo.nodo;
        nodeServer = new NodeServer(thisNode);
        nodeServer.listNode = listNodes;
        //Thread.sleep(10000);
        //set address/port successivo e precedente
        setterPointers();

        bufferimpl = new BufferImpl();
        pm10Simulator = new PM10Simulator(bufferimpl);
        pm10Simulator.start();


        //in parallelo
        Thread threadDelete = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("to delete press q");
                String scelta = in.next();

                if (scelta.equals("q")) {
                    log.info("deletion in progress");
                    NodeDelete deleteNodo = new NodeDelete(nodeServer, client, thisNode);
                    // se è l'unico nodo all'interno della rete chiude direttamente
                    if (nodeServer.isAlone) {
                        deleteNodo.start();
                        synchronized (nodeServer.lockExit) {
                            try {
                                nodeServer.lockExit.wait();
                                System.exit(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!nodeServer.isAlone) {
                        nodeServer.isDeleted = true;
                        synchronized (nodeServer.lockDelete) {
                            try {
                                nodeServer.lockDelete.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        deleteNodo.start();
                        synchronized (nodeServer.lockExit) {
                            try {
                                nodeServer.lockExit.wait();
                                //Thread.sleep(10000);
                                SendMessageToNextAndChangePrevious(nodeServer.previousAddress, nodeServer.previousPort, thisNode.getId());
                                SendMessageToPreviousAndChangeNext(nodeServer.nextAddress, nodeServer.nextPort, thisNode.getId());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        synchronized (nodeServer.lockWaitToken) {
                            nodeServer.lockWaitToken.notify();
                        }

                        synchronized (nodeServer.lockBye){
                            try {
                                nodeServer.lockBye.wait();
                                System.exit(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


            }
        });

        threadDelete.start();

        tokenManager = new TokenManager(nodeServer, thisNode, bufferimpl);
        tokenManager.start();

        synchronized (nodeServer.lockStartedTokenManager) {
            nodeServer.lockStartedTokenManager.wait();
        }

        if (nodeServer.isAlone) {
            nodeServer.listStatistics = new ArrayList<>();
            synchronized (nodeServer.lockToken) {
                nodeServer.lockToken.notify();
            }
        }

        while (!nodeServer.sendingStats) {
            synchronized (nodeServer.lockSending) {
                nodeServer.lockSending.wait();
                if (nodeServer.sendingStats)
                    sendStatistics();
            }
        }
    }

    private static void sendStatistics() throws IOException, InterruptedException {
        double sum = 0;
        long time = 0;

        //media valori
        for (Token.TokenMessage toke : nodeServer.listStatistics) {
            sum = sum + toke.getValue();
            time = toke.getTimestamp();
        }

        sum = sum / nodeServer.listStatistics.size();

        String stringSumAndTime = sum + "," + time;

        HttpRequest requestStatistica = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(stringSumAndTime))
                .uri(URI.create(URI_POST_STATISTIC))
                .setHeader("Content-Type", "text/plain")
                .setHeader("Accept", "application/json")
                .build();

        HttpResponse<String> responseStatistica = client.send(requestStatistica, HttpResponse.BodyHandlers.ofString());
        if (responseStatistica.statusCode() != 200) {
            System.out.println("HTTP ERROR:" + responseStatistica.statusCode());
        } else {
            System.out.println("Statistics sent " + sum + " " + time);
        }

        nodeServer.listStatistics = new ArrayList<>();
        nodeServer.sendingStats = false;

        synchronized (tokenManager.lock) {
            tokenManager.lock.notify();
        }
    }

    private static void setterPointers() {
        int pos = nodeServer.listNode.indexOf(thisNode);
        Node previousNode;
        Node nextNode;
        // è l'unico nodo
        if (nodeServer.listNode.size() == 1) {
            previousNode = nodeServer.listNode.get(0);
            nextNode = nodeServer.listNode.get(0);
            nodeServer.isAlone = true;
        } else {
            if (pos == 0)
                previousNode = nodeServer.listNode.get(nodeServer.listNode.size() - 1);
            else
                previousNode = nodeServer.listNode.get(pos - 1);
            if (pos == nodeServer.listNode.size() - 1)
                nextNode = nodeServer.listNode.get(0);
            else
                nextNode = nodeServer.listNode.get(pos + 1);
        }

        nodeServer.previousAddress = previousNode.getIpaddress();
        nodeServer.previousPort = previousNode.getPort();
        nodeServer.previousId = previousNode.getId();
        nodeServer.nextPort = nextNode.getPort();
        nodeServer.nextAddress = nextNode.getIpaddress();
        nodeServer.nextId = nextNode.getId();

        System.out.println("Next: " + nodeServer.nextPort);
        System.out.println("Previous: " + nodeServer.previousPort);

        if (nodeServer.listNode.size() > 1) {
            SendMessageToPreviousAndChangeNext(thisNode.getIpaddress(), thisNode.getPort(), thisNode.getId());
            SendMessageToNextAndChangePrevious(thisNode.getIpaddress(), thisNode.getPort(), thisNode.getId());
        }
    }

    public static void SendMessageToPreviousAndChangeNext(String addressToBeSet, int portToBeSet, int idToBeSet) {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(nodeServer.previousAddress + ":" + nodeServer.previousPort).usePlaintext().build();
        MessageChangeGrpc.MessageChangeBlockingStub stub = MessageChangeGrpc.newBlockingStub(channel);

        Message.requestChange requestChange = Message.requestChange.newBuilder()
                .setAddress(addressToBeSet)
                .setPort(portToBeSet)
                .setId(idToBeSet)
                .build();

        try {
            stub.changeNext(requestChange);
            System.out.println("[CLIENT/Change next] " + nodeServer.previousAddress + ":" + nodeServer.previousPort);
            channel.shutdown();
        } catch (StatusRuntimeException e) {
            int posP = 0;
            for (Node n : nodeServer.listNode) {
                if (n.getPort() == nodeServer.previousPort) {
                    posP = nodeServer.listNode.indexOf(n);
                    break;
                }
            }
            try {
                if (nodeServer.listNode.get(posP - 1) != null) {
                    nodeServer.previousPort = nodeServer.listNode.get(posP - 1).getPort();
                    nodeServer.previousAddress = nodeServer.listNode.get(posP - 1).getIpaddress();
                    nodeServer.previousId = nodeServer.listNode.get(posP - 1).getId();
                    SendMessageToPreviousAndChangeNext(thisNode.getIpaddress(), thisNode.getPort(), thisNode.getId());
                }
            } catch (IndexOutOfBoundsException exe) {
                Node nodeUlt = nodeServer.listNode.get(nodeServer.listNode.size() - 1);
                nodeServer.previousPort = nodeUlt.getPort();
                nodeServer.previousAddress = nodeUlt.getIpaddress();
                nodeServer.previousId = nodeUlt.getId();
                SendMessageToPreviousAndChangeNext(thisNode.getIpaddress(), thisNode.getPort(), thisNode.getId());
            }
        }
    }


    public static void SendMessageToNextAndChangePrevious(String addressToBeSet, int portToBeSet, int idToBeSet) {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(nodeServer.nextAddress + ":" + nodeServer.nextPort).usePlaintext().build();
        MessageChangeGrpc.MessageChangeBlockingStub stub = MessageChangeGrpc.newBlockingStub(channel);
        Message.requestChange requestChange = Message.requestChange.newBuilder()
                .setAddress(addressToBeSet)
                .setPort(portToBeSet)
                .setId(idToBeSet)
                .build();

        try {
            stub.changePrevious(requestChange);
            System.out.println("[CLIENT/Change previous] " + nodeServer.nextAddress + ":" + nodeServer.nextPort);
            channel.shutdown();
        } catch (StatusRuntimeException e) {
            int posN = 0;
            for (Node n : nodeServer.listNode) {
                if (n.getPort() == nodeServer.nextPort)
                    posN = nodeServer.listNode.indexOf(n);
            }
            try {
                if (nodeServer.listNode.get(posN + 1) != null) {
                    nodeServer.previousPort = nodeServer.listNode.get(posN + 1).getPort();
                    nodeServer.previousAddress = nodeServer.listNode.get(posN + 1).getIpaddress();
                    nodeServer.previousId = nodeServer.listNode.get(posN + 1).getId();
                    SendMessageToNextAndChangePrevious(thisNode.getIpaddress(), thisNode.getPort(), thisNode.getId());
                }
            } catch (IndexOutOfBoundsException exe) {
                Node nodeFirst = nodeServer.listNode.get(0);
                nodeServer.nextPort = nodeFirst.getPort();
                nodeServer.nextAddress = nodeFirst.getIpaddress();
                nodeServer.nextId = nodeFirst.getId();
                SendMessageToNextAndChangePrevious(thisNode.getIpaddress(), thisNode.getPort(), thisNode.getId());
            }
        }
    }
}

