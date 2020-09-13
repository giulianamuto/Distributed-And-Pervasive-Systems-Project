package nodo;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.beans.Node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;

public class NodeAdded extends Thread {
    public Node nodo;
    public int id;
    public int port;
    public int portGateway;
    public String ipAddress;

    private HttpClient httpClient;
    private Object lockAdd;

    public List<Node> listNodes = new ArrayList<>();

    private final String URI_POST_NODO = "http://localhost:6060/gatewayNode/addNode";

    private static final Logger log = Logger.getLogger("global");

    public NodeAdded(HttpClient httpClient, Object lockAdd) {
        this.lockAdd = lockAdd;
        this.httpClient = httpClient;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        // ipAddress = "127.0.0.1";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Ip Address: " + ipAddress);

        boolean portGatewayisOk = false;
        while (!portGatewayisOk) {
            System.out.println("Gateway port: ");
            portGateway = in.nextInt();
            // System.out.println(portGateway);
            if (portGateway == 6060)
                portGatewayisOk = true;
        }

        boolean isPresent = true;

        while (isPresent) {
            System.out.println("ID: ");
            id = in.nextInt();
            System.out.println("Port: ");
            port = in.nextInt();

            String all = "" + id + "," + port + "," + ipAddress + "," + portGateway;
            //System.out.println(all);
            HttpRequest requestPOST = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(all))
                    .uri(URI.create(URI_POST_NODO))
                    .setHeader("Content-Type", "text/plain")
                    .setHeader("Accept", "application/json")
                    .build();

            HttpResponse<String> responsePOST = null;
            try {

                responsePOST = httpClient.send(requestPOST, HttpResponse.BodyHandlers.ofString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (responsePOST.statusCode() != 200) {
                System.out.println("try again with another port or id: " + responsePOST.statusCode());
            } else {

                isPresent = false;
                System.out.println(responsePOST.body());

                String json = responsePOST.body();
                ObjectMapper mapper = new ObjectMapper();

                try {
                    listNodes.addAll(Arrays.asList(mapper.readValue(json, Node[].class)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nodo = listNodes.get(listNodes.size() - 1);
            }
        }

        sortListID();

        synchronized (lockAdd) {
            lockAdd.notify();
        }
    }

    private void sortListID() {

        if (listNodes.size() > 1) {
            Collections.sort(listNodes, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.id - o2.id;
                }
            });
        }
    }
}


