package messageAnalystproto;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.7.0)",
    comments = "Source: MessageAnalyst.proto")
public final class PushNotificationsGrpc {

  private PushNotificationsGrpc() {}

  public static final String SERVICE_NAME = "messageAnalystproto.PushNotifications";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<messageAnalystproto.MessageAnalyst.MessageForAnalyst,
      messageAnalystproto.MessageAnalyst.MessageForAnalyst> METHOD_NOTIFICATIONS =
      io.grpc.MethodDescriptor.<messageAnalystproto.MessageAnalyst.MessageForAnalyst, messageAnalystproto.MessageAnalyst.MessageForAnalyst>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "messageAnalystproto.PushNotifications", "notifications"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              messageAnalystproto.MessageAnalyst.MessageForAnalyst.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              messageAnalystproto.MessageAnalyst.MessageForAnalyst.getDefaultInstance()))
          .setSchemaDescriptor(new PushNotificationsMethodDescriptorSupplier("notifications"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PushNotificationsStub newStub(io.grpc.Channel channel) {
    return new PushNotificationsStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PushNotificationsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PushNotificationsBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PushNotificationsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PushNotificationsFutureStub(channel);
  }

  /**
   */
  public static abstract class PushNotificationsImplBase implements io.grpc.BindableService {

    /**
     */
    public void notifications(messageAnalystproto.MessageAnalyst.MessageForAnalyst request,
        io.grpc.stub.StreamObserver<messageAnalystproto.MessageAnalyst.MessageForAnalyst> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_NOTIFICATIONS, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_NOTIFICATIONS,
            asyncUnaryCall(
              new MethodHandlers<
                messageAnalystproto.MessageAnalyst.MessageForAnalyst,
                messageAnalystproto.MessageAnalyst.MessageForAnalyst>(
                  this, METHODID_NOTIFICATIONS)))
          .build();
    }
  }

  /**
   */
  public static final class PushNotificationsStub extends io.grpc.stub.AbstractStub<PushNotificationsStub> {
    private PushNotificationsStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PushNotificationsStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PushNotificationsStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PushNotificationsStub(channel, callOptions);
    }

    /**
     */
    public void notifications(messageAnalystproto.MessageAnalyst.MessageForAnalyst request,
        io.grpc.stub.StreamObserver<messageAnalystproto.MessageAnalyst.MessageForAnalyst> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_NOTIFICATIONS, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PushNotificationsBlockingStub extends io.grpc.stub.AbstractStub<PushNotificationsBlockingStub> {
    private PushNotificationsBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PushNotificationsBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PushNotificationsBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PushNotificationsBlockingStub(channel, callOptions);
    }

    /**
     */
    public messageAnalystproto.MessageAnalyst.MessageForAnalyst notifications(messageAnalystproto.MessageAnalyst.MessageForAnalyst request) {
      return blockingUnaryCall(
          getChannel(), METHOD_NOTIFICATIONS, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PushNotificationsFutureStub extends io.grpc.stub.AbstractStub<PushNotificationsFutureStub> {
    private PushNotificationsFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PushNotificationsFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PushNotificationsFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PushNotificationsFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<messageAnalystproto.MessageAnalyst.MessageForAnalyst> notifications(
        messageAnalystproto.MessageAnalyst.MessageForAnalyst request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_NOTIFICATIONS, getCallOptions()), request);
    }
  }

  private static final int METHODID_NOTIFICATIONS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PushNotificationsImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PushNotificationsImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_NOTIFICATIONS:
          serviceImpl.notifications((messageAnalystproto.MessageAnalyst.MessageForAnalyst) request,
              (io.grpc.stub.StreamObserver<messageAnalystproto.MessageAnalyst.MessageForAnalyst>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PushNotificationsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PushNotificationsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return messageAnalystproto.MessageAnalyst.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PushNotifications");
    }
  }

  private static final class PushNotificationsFileDescriptorSupplier
      extends PushNotificationsBaseDescriptorSupplier {
    PushNotificationsFileDescriptorSupplier() {}
  }

  private static final class PushNotificationsMethodDescriptorSupplier
      extends PushNotificationsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PushNotificationsMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PushNotificationsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PushNotificationsFileDescriptorSupplier())
              .addMethod(METHOD_NOTIFICATIONS)
              .build();
        }
      }
    }
    return result;
  }
}
