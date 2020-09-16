package nodo;

import gateway.beans.Node;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class NodeDelete extends Thread {

    private NodeServer nodeServer;
    private HttpClient client;
    private Node node;

    private static final String URI_DELETE_NODO = "http://localhost:6060/gatewayNode/deleteNode";

    private static final Logger log = Logger.getLogger("global");

    public NodeDelete(NodeServer nodeServer, HttpClient client, Node node) {
        this.nodeServer = nodeServer;
        this.client = client;
        this.node = node;
    }

    @Override
    public void run() {
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

        synchronized (nodeServer.lockExit) {
            nodeServer.lockExit.notify();
        }
    }
}

