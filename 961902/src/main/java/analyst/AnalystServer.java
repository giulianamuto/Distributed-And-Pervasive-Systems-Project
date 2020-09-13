package analyst;

import gateway.beans.Analyst;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import messageAnalystproto.MessageAnalyst;
import messageAnalystproto.PushNotificationsGrpc;

import java.io.IOException;

public class AnalystServer {

    private Server server;
    private Analyst analyst;

    public AnalystServer(Analyst analyst) {
        this.analyst = analyst;
        startServer();
    }

    public void startServer() {
        server = ServerBuilder.forPort(analyst.getPort())
                .addService(new Notifications())
                .build();

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER] Connected: " + server.getPort());
        System.out.println("---------");
    }

    private class Notifications extends PushNotificationsGrpc.PushNotificationsImplBase {

        public void notifications(MessageAnalyst.MessageForAnalyst request, StreamObserver<MessageAnalyst.MessageForAnalyst> streamObserver) {
            String message = "" + request.getMsg();
            System.out.println(" " + request.getMsg());
            MessageAnalyst.MessageForAnalyst response =
                    MessageAnalyst.MessageForAnalyst.newBuilder()
                            .setMsg(message)
                            .build();
            streamObserver.onNext(response);
            streamObserver.onCompleted();
        }
    }

}



