package analyst;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.beans.Analyst;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import messageAnalystproto.MessageAnalyst;
import messageAnalystproto.PushNotificationsGrpc;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PushNotification extends Thread {

    private static final String URI_GET_ANALYSTS = "http://localhost:6060/gatewayAnalyst/getAnalysts";
    private List<Analyst> listAnalysts;
    private String message;

    public PushNotification(List<Analyst> listAnalysts, String message) {
        this.listAnalysts = listAnalysts;
        this.message = message;
    }


    @Override
    public void run() {
        HttpClient client = HttpClient.newHttpClient();
        listAnalysts = new ArrayList<>();
        HttpRequest requestPOST = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URI_GET_ANALYSTS))
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
            System.out.println("error: " + responsePOST.statusCode());
        } else {

            // System.out.println(responsePOST.body());
            String json = responsePOST.body();
            ObjectMapper mapper = new ObjectMapper();
            try {
                listAnalysts.addAll(Arrays.asList(mapper.readValue(json, Analyst[].class)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Analyst a : listAnalysts) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(a.getIpAddress() + ":" + a.getPort()).usePlaintext().build();
            PushNotificationsGrpc.PushNotificationsBlockingStub stub = PushNotificationsGrpc.newBlockingStub(channel);
            MessageAnalyst.MessageForAnalyst messageForAnalyst = MessageAnalyst.MessageForAnalyst.newBuilder()
                    .setMsg(message)
                    .build();
            stub.notifications(messageForAnalyst);
            channel.shutdown();
        }
    }

}
