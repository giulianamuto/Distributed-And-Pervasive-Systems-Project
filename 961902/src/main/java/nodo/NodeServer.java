package nodo;

import gateway.beans.Node;
import gateway.list.ListAnalyst;
import gateway.list.ListNodes;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import messageproto.Message;
import messageproto.MessageChangeGrpc;
import tokenproto.SendTokenGrpc;
import tokenproto.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NodeServer {
    private static Server server;
    private Node node;

    public int previousPort = 0;
    public String previousAddress = null;
    public int previousId = 0;

    public int nextPort = 0;
    public String nextAddress = null;
    public int nextId = 0;

    public List<Token.TokenMessage> listStatistics;
    public List<Node> listNode;

    //Bool
    public boolean sendingStats = false; // invio statistiche
    public boolean retrySend = false; // rifai tutto
    public boolean isAlone = false; // l'unico nodo
    public boolean isDeleted = false; // processo di eliminazione avviato
    public boolean isUltimate = false; // ultimo nodo
    public boolean waitTokenAgain = false;
    public boolean sendTokenLastTime = false;
    public boolean hasToken = false;

    //lock
    public final Object lockToken = new Object();
    public final Object lockWaitTokenAgain = new Object();
    public final Object lockStartTOkenManager = new Object();


    public NodeServer(Node nodo) {
        this.node = nodo;
        startServer();
    }

    public void startServer() {
        server = ServerBuilder.forPort(node.getPort())
                .addService(new changeNodes())
                .addService(new TokenServer())
                .build();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER] Connected: " + server.getPort());
        System.out.println("---------");
    }

    public class changeNodes extends MessageChangeGrpc.MessageChangeImplBase {

        public void changeNext(Message.requestChange request, StreamObserver<Message.responseChange> streamObserver) {
            nextAddress = request.getAddress();
            nextPort = request.getPort();
            nextId = request.getId();
            System.out.println("Next: " + nextAddress + ":  " + nextPort);
            isUltimate = false;
            Message.responseChange response = Message.responseChange.newBuilder()
                    .setResponse(nextPort + nextAddress)
                    .build();

            streamObserver.onNext(response);

            if (nextPort == node.getPort() && previousPort == node.getPort()) {
                isAlone = true;
                isUltimate = true;
            } else isAlone = false;

            boolean check = false;
            for (Node nod : listNode) {
                if (nod.getId() == previousId) {
                    check = false;
                    break;
                } else check = true;
            }
            if (check) {
                Node n = new Node(nextId, nextPort, nextAddress, 0);
                listNode.add(n);
                sortID();
            }
            streamObserver.onCompleted();
        }

        public void changePrevious(Message.requestChange request, StreamObserver<Message.responseChange> streamObserver) {
            previousAddress = request.getAddress();
            previousPort = request.getPort();
            previousId = request.getId();

            System.out.println("Previous: " + previousAddress + ":  " + previousPort);
            Message.responseChange response = Message.responseChange.newBuilder()
                    .setResponse(previousPort + previousAddress)
                    .build();

            streamObserver.onNext(response);

            if (nextPort == node.getPort() && previousPort == node.getPort()) {
                isAlone = true;
                isUltimate = true;
            } else isAlone = false;

            boolean check = false;
            for (Node nod : listNode) {
                if (nod.getId() == previousId) {
                    check = false;
                    break;
                } else check = true;
            }
            if (check) {
                Node n = new Node(previousId, previousPort, previousAddress, 0);
                listNode.add(n);
                sortID();
            }
            streamObserver.onCompleted();
        }

        private void sortID() {
            if (listNode.size() > 1) {
                Collections.sort(listNode, new Comparator<Node>() {
                    @Override
                    public int compare(Node o1, Node o2) {
                        return o1.id - o2.id;
                    }
                });
            }
        }
    }


    public class TokenServer extends SendTokenGrpc.SendTokenImplBase {

        public void sendToken(Token.tokenRequest request, StreamObserver<Token.message> streamObserver) {

            Token.message message;
            System.out.println("[Token Received]");
            if (nextPort == listNode.get(0).getPort() || node.getId() > nextId)
                isUltimate = true;
            // System.out.println("ulti: " + isUltimate);
            listStatistics = new ArrayList<>();

            for (Token.TokenMessage req : request.getTokenList()) {
                listStatistics.add(req);
            }
            message = Token.message.newBuilder()
                    .setFlag(true)
                    .build();

            hasToken = true;

            if (waitTokenAgain)
                sendTokenLastTime = true;
            streamObserver.onNext(message);
            synchronized (lockToken) {
                lockToken.notify();
            }

            streamObserver.onCompleted();
        }
    }
}
