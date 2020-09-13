import analyst.AnalystServer;
import gateway.beans.Analyst;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class MainAnalyst {

    private static final String URI_CALCULATE = "http://localhost:6060/gatewayAnalyst/calculateStatistics/";
    private static final String URI_GET_STATISTICS = "http://localhost:6060/gatewayAnalyst/getStatistics/";
    private static final String URI_GET_NODES = "http://localhost:6060/gatewayAnalyst/getNumberNodes";
    private static final String URI_POST_ANALYST = "http://localhost:6060/gatewayAnalyst/addAnalyst";
    private static final String URI_DELETE_ANALYST = "http://localhost:6060/gatewayAnalyst/deleteAnalyst";

    private static int id;
    private static int port;
    private static String ipAddress;
    private static Analyst analyst;
    private static HttpClient client;
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {

        client = HttpClient.newHttpClient();
        added();
        AnalystServer analystServer = new AnalystServer(analyst);

        boolean loop = true;
        do {
            System.out.println("Welcome Analyst");
            System.out.println("To press:");
            System.out.println("1. To get the statistics");
            System.out.println("2. To calculate the statistics");
            System.out.println("3. To get the numbers of nodes");
            System.out.println("other to go out");

            String choice = in.next();

            switch (choice) {
                case "1":
                    System.out.println("Insert number: ");
                    String s = in.next();
                    HttpRequest requestGET = HttpRequest.newBuilder()
                            .method("GET", HttpRequest.BodyPublishers.ofString(s))
                            .uri(URI.create(URI_GET_STATISTICS + "" + s))
                            .setHeader("Content-Type", "text/plain")
                            .setHeader("Accept", "text/plain")
                            .build();
                    HttpResponse<String> responseGet = client.send(requestGET, HttpResponse.BodyHandlers.ofString());

                    if (responseGet.statusCode() != 200) {
                        System.out.println("HTTP ERROR:" + responseGet.statusCode());
                    } else {
                        System.out.println("here are your stats:  " + responseGet.body());
                    }
                    continue;

                case "2":
                    System.out.println("Insert number: ");
                    String n = in.next();
                    HttpRequest requestCALCOLO = HttpRequest.newBuilder()
                            .method("GET", HttpRequest.BodyPublishers.ofString(n))
                            .uri(URI.create(URI_CALCULATE + "" + n))
                            .setHeader("Content-Type", "text/plain")
                            .setHeader("Accept", "text/plain")
                            .build();
                    HttpResponse<String> responseCalcolo = client.send(requestCALCOLO, HttpResponse.BodyHandlers.ofString());

                    if (responseCalcolo.statusCode() != 200) {
                        System.out.println("HTTP ERROR:" + responseCalcolo.statusCode());
                    } else {
                        System.out.println(responseCalcolo.body());
                    }
                    break;

                case "3":
                    System.out.println("Number of Nodes present in the system: ");
                    HttpRequest requestNodo = HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(URI_GET_NODES))
                            .setHeader("Accept", "text/plain")
                            .build();
                    HttpResponse<String> responseNodo = client.send(requestNodo, HttpResponse.BodyHandlers.ofString());

                    if (responseNodo.statusCode() != 200) {
                        System.out.println("HTTP ERROR:" + responseNodo.statusCode());
                    }
                    System.out.println(responseNodo.body());
                    break;

                default:
                    loop = false;
                    break;
            }
        } while (loop);

        removed();
    }

    private static void removed() {
        String sid = "" + id;
        HttpRequest requestDELETE = HttpRequest.newBuilder()
                .method("DELETE", HttpRequest.BodyPublishers.ofString(sid))
                .uri(URI.create(URI_DELETE_ANALYST))
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
        System.out.println("Bye bye");
        System.exit(0);
    }


    public static void added() {

        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Ip Address: " + ipAddress);

        boolean isPresent = true;

        while (isPresent) {
            System.out.println("ID: ");
            id = in.nextInt();
            System.out.println("Port: ");
            port = in.nextInt();


            String all = "" + port + "," + ipAddress + "," + id;
            //System.out.println(all);
            HttpRequest requestPOST = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(all))
                    .uri(URI.create(URI_POST_ANALYST))
                    .setHeader("Content-Type", "text/plain")
                    .setHeader("Accept", "application/json")
                    .build();

            HttpResponse<String> responsePOST = null;
            try {

                responsePOST = client.send(requestPOST, HttpResponse.BodyHandlers.ofString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (responsePOST.statusCode() != 200) {
                System.out.println("try again with another id or port: " + responsePOST.statusCode());
            } else {

                isPresent = false;
                System.out.println(responsePOST.body());
                analyst = new Analyst(port, ipAddress, id);
            }

        }
    }
}


