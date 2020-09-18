package nodo;

import gateway.beans.Node;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import simulatore.BufferImpl;
import simulatore.Measurement;
import tokenproto.SendTokenGrpc;
import tokenproto.Token;

import java.util.List;
import java.util.logging.Logger;


public class TokenManager extends Thread {

    private NodeServer nodeServer;
    private Node node;
    private BufferImpl bufferimpl;
    private List<Token.TokenMessage> listStatistics;

    public Object lock = new Object();

    private static final Logger log = Logger.getLogger("global");

    public TokenManager(NodeServer nodeServer, Node node, BufferImpl bufferimpl) {
        this.nodeServer = nodeServer;
        this.node = node;
        this.bufferimpl = bufferimpl;
    }

    /*
      Prendo statistiche
      InsertStatistics
      ToSendServer
        true: invio
      Sono l'unico nodo
        true: ripeti
        false: send Token
            true: ok
            false: il nodo suc si sta eliminando
                checkNodo
    */

    @Override
    public void run() {

        synchronized (nodeServer.lockStartedTokenManager) {
            nodeServer.lockStartedTokenManager.notify();
        }

        if (nodeServer.hasToken) {
            tokenHendeler();
        }

        while (nodeServer.listStatistics == null) {
            synchronized (nodeServer.lockToken) {
                try {
                    nodeServer.lockToken.wait();
                    if (nodeServer.listStatistics != null || nodeServer.hasToken) {
                        Thread.sleep(800);
                        tokenHendeler();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void tokenHendeler() {
        do {
            if (nodeServer.isAlone)
                nodeServer.isUltimateNode = true;
            double value;
            long time;

            listStatistics = nodeServer.listStatistics;

            Measurement measurement = bufferimpl.measurementDTOQueue.poll();

            if (measurement == null) {
                time = 0;
                value = 0;
            } else {
                time = measurement.getTimestamp();
                value = measurement.getValue();
            }

            insertStatistics(time, value);

            //  log.info("m" + measurement);
            if (nodeServer.isUltimateNode) {
                if (toSendServer()) {
                    // log.info("Invio statistiche");
                    nodeServer.sendingStats = true;
                    synchronized (nodeServer.lockSending) {
                        nodeServer.lockSending.notify();
                    }
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (nodeServer.isEliminating) {
                if (!listStatistics.isEmpty()) {
                    for (Token.TokenMessage tok : listStatistics) {
                        if (tok.getPort() == node.getPort()) {
                            if (tok.getValue() == 0) {
                                listStatistics.remove(tok);
                                break;
                            }
                        }
                    }
                }
                synchronized (nodeServer.lockDelete) {
                    nodeServer.lockDelete.notify();
                }
                synchronized (nodeServer.lockWaitToken) {
                    try {
                        nodeServer.lockWaitToken.wait();
                        sendToken(nodeServer.nextPort, nodeServer.nextAddress);
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // se sono il primo ripeti
            if (nodeServer.isAlone)
                nodeServer.retrySend = true;
            else
                sendToken(nodeServer.nextPort, nodeServer.nextAddress);

        } while (nodeServer.retrySend);
    }

    private void sendToken(int port, String address) {

        if (nodeServer.isAlone) {
            nodeServer.retrySend = true;
        } else {

            Token.tokenRequest tokenRequest = Token.tokenRequest.newBuilder()
                    .addAllToken(listStatistics)
                    .build();

            final ManagedChannel channel = ManagedChannelBuilder.forTarget(address + ":" + port).usePlaintext().build();
            System.out.println("[Token Send] " + address + ": " + port);
            SendTokenGrpc.SendTokenBlockingStub stub = SendTokenGrpc.newBlockingStub(channel);

            Token.message message = null;
            try {
                message = stub.sendToken(tokenRequest);
            } catch (StatusRuntimeException e) {
                //sendToken(nodeServer.nextPort, nodeServer.nextAddress);
                System.out.println("Sono qui");
            }
            channel.shutdown();

            try {
                if (message.getFlag()) {
                    nodeServer.listStatistics = null;
                    nodeServer.retrySend = false;
                    nodeServer.hasToken = false;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (nodeServer.isEliminating) {
            synchronized (nodeServer.lockBye) {
                nodeServer.lockBye.notify();
            }
        }
    }

    /*
      Inserisco la statistica:
       Lista vuota
         true: inserisco
          else: Controllo se esiste gia una statistica con la porta uguale al nodo
                 true: sovrascrivo i dati
                 else: inserisco i dati
    */
    private void insertStatistics(long time, double value) {
        boolean isPresent = false;
        Token.TokenMessage tokenMessage = null;

        if (listStatistics.isEmpty()) {
            tokenMessage = Token.TokenMessage.newBuilder()
                    .setValue(value)
                    .setTimestamp(time)
                    .setPort(node.getPort())
                    .setAddress(node.getIpaddress())
                    .build();
            listStatistics.add(tokenMessage);
        } else {
            for (Token.TokenMessage e : listStatistics) {
                if (e.getPort() == node.getPort()) {
                    //log.info("trovato, metto statistiche");
                    listStatistics.remove(e);
                    tokenMessage = Token.TokenMessage.newBuilder()
                            .setValue(value)
                            .setTimestamp(time)
                            .setPort(node.getPort())
                            .setAddress(node.getIpaddress())
                            .build();
                    listStatistics.add(tokenMessage);
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent) {
                tokenMessage = Token.TokenMessage.newBuilder()
                        .setValue(value)
                        .setTimestamp(time)
                        .setPort(node.getPort())
                        .setAddress(node.getIpaddress())
                        .build();
                listStatistics.add(tokenMessage);
            }
        }
        //   System.out.println(tokenMessage.toString());
    }

    /*
       true: puoi inviare le statistiche
       false: non puoi inviare le statistiche
   */
    private boolean toSendServer() {
        boolean check = true;
        for (Token.TokenMessage e : listStatistics) {
            if (e.getValue() == 0) {
                check = false;
                break;
            }
        }
        return check;
    }


}