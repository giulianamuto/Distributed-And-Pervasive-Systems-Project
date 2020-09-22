package gateway.pushNot;

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

    private List<Analyst> listAnalysts;
    private String message;

    public PushNotification(List<Analyst> listAnalysts, String message) {
        this.listAnalysts = listAnalysts;
        this.message = message;
    }

    @Override
    public void run() {
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
