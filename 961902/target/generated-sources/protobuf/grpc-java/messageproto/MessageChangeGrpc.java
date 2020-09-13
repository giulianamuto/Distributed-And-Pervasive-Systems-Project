package messageproto;

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
    comments = "Source: Message.proto")
public final class MessageChangeGrpc {

  private MessageChangeGrpc() {}

  public static final String SERVICE_NAME = "messageproto.MessageChange";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<messageproto.Message.requestChange,
      messageproto.Message.responseChange> METHOD_CHANGE_NEXT =
      io.grpc.MethodDescriptor.<messageproto.Message.requestChange, messageproto.Message.responseChange>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "messageproto.MessageChange", "changeNext"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              messageproto.Message.requestChange.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              messageproto.Message.responseChange.getDefaultInstance()))
          .setSchemaDescriptor(new MessageChangeMethodDescriptorSupplier("changeNext"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<messageproto.Message.requestChange,
      messageproto.Message.responseChange> METHOD_CHANGE_PREVIOUS =
      io.grpc.MethodDescriptor.<messageproto.Message.requestChange, messageproto.Message.responseChange>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "messageproto.MessageChange", "changePrevious"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              messageproto.Message.requestChange.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              messageproto.Message.responseChange.getDefaultInstance()))
          .setSchemaDescriptor(new MessageChangeMethodDescriptorSupplier("changePrevious"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MessageChangeStub newStub(io.grpc.Channel channel) {
    return new MessageChangeStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MessageChangeBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MessageChangeBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MessageChangeFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MessageChangeFutureStub(channel);
  }

  /**
   */
  public static abstract class MessageChangeImplBase implements io.grpc.BindableService {

    /**
     */
    public void changeNext(messageproto.Message.requestChange request,
        io.grpc.stub.StreamObserver<messageproto.Message.responseChange> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CHANGE_NEXT, responseObserver);
    }

    /**
     */
    public void changePrevious(messageproto.Message.requestChange request,
        io.grpc.stub.StreamObserver<messageproto.Message.responseChange> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CHANGE_PREVIOUS, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_CHANGE_NEXT,
            asyncUnaryCall(
              new MethodHandlers<
                messageproto.Message.requestChange,
                messageproto.Message.responseChange>(
                  this, METHODID_CHANGE_NEXT)))
          .addMethod(
            METHOD_CHANGE_PREVIOUS,
            asyncUnaryCall(
              new MethodHandlers<
                messageproto.Message.requestChange,
                messageproto.Message.responseChange>(
                  this, METHODID_CHANGE_PREVIOUS)))
          .build();
    }
  }

  /**
   */
  public static final class MessageChangeStub extends io.grpc.stub.AbstractStub<MessageChangeStub> {
    private MessageChangeStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessageChangeStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageChangeStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessageChangeStub(channel, callOptions);
    }

    /**
     */
    public void changeNext(messageproto.Message.requestChange request,
        io.grpc.stub.StreamObserver<messageproto.Message.responseChange> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CHANGE_NEXT, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void changePrevious(messageproto.Message.requestChange request,
        io.grpc.stub.StreamObserver<messageproto.Message.responseChange> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CHANGE_PREVIOUS, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MessageChangeBlockingStub extends io.grpc.stub.AbstractStub<MessageChangeBlockingStub> {
    private MessageChangeBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessageChangeBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageChangeBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessageChangeBlockingStub(channel, callOptions);
    }

    /**
     */
    public messageproto.Message.responseChange changeNext(messageproto.Message.requestChange request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CHANGE_NEXT, getCallOptions(), request);
    }

    /**
     */
    public messageproto.Message.responseChange changePrevious(messageproto.Message.requestChange request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CHANGE_PREVIOUS, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MessageChangeFutureStub extends io.grpc.stub.AbstractStub<MessageChangeFutureStub> {
    private MessageChangeFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessageChangeFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageChangeFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessageChangeFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<messageproto.Message.responseChange> changeNext(
        messageproto.Message.requestChange request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CHANGE_NEXT, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<messageproto.Message.responseChange> changePrevious(
        messageproto.Message.requestChange request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CHANGE_PREVIOUS, getCallOptions()), request);
    }
  }

  private static final int METHODID_CHANGE_NEXT = 0;
  private static final int METHODID_CHANGE_PREVIOUS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MessageChangeImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MessageChangeImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHANGE_NEXT:
          serviceImpl.changeNext((messageproto.Message.requestChange) request,
              (io.grpc.stub.StreamObserver<messageproto.Message.responseChange>) responseObserver);
          break;
        case METHODID_CHANGE_PREVIOUS:
          serviceImpl.changePrevious((messageproto.Message.requestChange) request,
              (io.grpc.stub.StreamObserver<messageproto.Message.responseChange>) responseObserver);
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

  private static abstract class MessageChangeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MessageChangeBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return messageproto.Message.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MessageChange");
    }
  }

  private static final class MessageChangeFileDescriptorSupplier
      extends MessageChangeBaseDescriptorSupplier {
    MessageChangeFileDescriptorSupplier() {}
  }

  private static final class MessageChangeMethodDescriptorSupplier
      extends MessageChangeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MessageChangeMethodDescriptorSupplier(String methodName) {
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
      synchronized (MessageChangeGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MessageChangeFileDescriptorSupplier())
              .addMethod(METHOD_CHANGE_NEXT)
              .addMethod(METHOD_CHANGE_PREVIOUS)
              .build();
        }
      }
    }
    return result;
  }
}
