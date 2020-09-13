package nodo;

import gateway.beans.Node;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;

public class NodeDelete extends Thread {

    private NodeServer nodoServer;
    private HttpClient client;
    private Node node;
    private Object lockWaitDelete;

    private static final String URI_DELETE_NODO = "http://localhost:6060/gatewayNode/deleteNode";

    private static final Logger log = Logger.getLogger("global");

    public NodeDelete(NodeServer nodoServer, HttpClient client, Node node, Object lockWaitDelete) {
        this.nodoServer = nodoServer;
        this.client = client;
        this.node = node;
        this.lockWaitDelete = lockWaitDelete;
    }

    @Override
    public void run() {
        deletedNode();
        synchronized (lockWaitDelete) {
            lockWaitDelete.notify();
        }
    }

    private void deletedNode() {
        String stringID = "" + node.getId();

        HttpRequest requestDELETE = HttpRequest.newBuilder()
                .method("DELETE", HttpRequest.BodyPublishers.ofString(stringID))
                .uri(URI.create(URI_DELETE_NODO))
                .setHeader("Content-Type", "text/plain").build();

        HttpResponse<String> responseDELETE = null;

        try {
            responseDELETE = client.send(requestDELETE, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (responseDELETE.statusCode() != 200) {
            System.out.println("HTTP ERROR:" + responseDELETE.statusCode());
        }

        // se è l'unico nodo all'interno della rete chiude direttamente
        if (nodoServer.nextPort == node.getPort() && nodoServer.previousPort == node.getPort()) {
            log.info("Deleted");
            System.exit(0);
        }
    }
}

