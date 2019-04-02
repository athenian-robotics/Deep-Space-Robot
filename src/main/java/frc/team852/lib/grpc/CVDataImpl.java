package frc.team852.lib.grpc;

import com.google.protobuf.Empty;
import frc.team852.DeepSpaceRobot.CameraConfig;
import frc.team852.DeepSpaceRobot.CameraPose;
import frc.team852.DeepSpaceRobot.OpenCVInfoGrpc;
import frc.team852.Robot;
import frc.team852.lib.callbacks.CameraPoseListener;
import frc.team852.lib.callbacks.GenericListener;
import io.grpc.stub.StreamObserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class CVDataImpl extends OpenCVInfoGrpc.OpenCVInfoImplBase {
  private Map<Class, ConcurrentLinkedQueue<GenericListener>> allCallbacks = new ConcurrentHashMap<>();

  @Override
  public void sendCameraPose(CameraPose request, StreamObserver<Empty> responseObserver) {
    Empty reply = Empty.newBuilder().build();
    System.out.print(request);
    allCallbacks.computeIfAbsent(CameraPose.class, k -> new ConcurrentLinkedQueue<>());
    try {
      allCallbacks.get(CameraPoseListener.class).forEach(c -> ((CameraPoseListener) c).onNewData(request, responseObserver));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("STOP BEING A POSER, FIX YOUR CALLBACK");
    }
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }


  @Override
  public void getCameraConfig(Empty request, StreamObserver<CameraConfig> responseObserver) {
    int camIdx = Robot.VIDEO_STREAM_IDX;
    int flipCode = Robot.VIDEO_STREAM_FLIP_CODE;

    responseObserver.onNext(CameraConfig.newBuilder().setFlipCode(flipCode).setIndex(camIdx).build());
  }

  public synchronized void registerCallback(GenericListener listener) {
    if (listener.msgType == null) {
      throw new RuntimeException("Listener's message type is null");
    }
    allCallbacks.computeIfAbsent(listener.msgType, k -> new ConcurrentLinkedQueue<>());
    allCallbacks.get(listener.msgType).add(listener);
  }

//  @Override
//  public void sendBall(Ball request, StreamObserver<Empty> responseObserver) {
//    Empty reply = Empty.newBuilder().build();
//    System.out.print(request);
//    this.store.ballRef.set(request);
//    allCallbacks.computeIfAbsent(Ball.class, k -> new ConcurrentLinkedQueue<>());
//    try {
//      allCallbacks.get(BallListener.class).forEach(c -> ((BallListener) c).onNewData(request));
//    } catch (Exception e) {
//      e.printStackTrace();
//      System.out.println("FIX YOUR CALLBACK YOU LOUT");
//    }
//    responseObserver.onNext(reply);
//    responseObserver.onCompleted();
//  }
//
//  @Override
//  public void sendHatch(Hatch request, StreamObserver<Empty> responseObserver) {
//    Empty reply = Empty.newBuilder().build();
//    System.out.print(request);
//    this.store.hatchRef.set(request);
//    allCallbacks.computeIfAbsent(Hatch.class, k -> new ConcurrentLinkedQueue<>());
//    try {
//      allCallbacks.get(HatchListener.class).forEach(c -> ((HatchListener) c).onNewData(request));
//    } catch (Exception e) {
//      e.printStackTrace();
//      System.out.println("HEY BURRITO BRAINS, FIX YOUR CALLBACK");
//    }
//    responseObserver.onNext(reply);
//    responseObserver.onCompleted();
//  }
//
//  @Override
//  public void sendFrameSize(FrameSize request, StreamObserver<Empty> responseObserver) {
//    Empty reply = Empty.newBuilder().build();
//    System.out.print(request);
//    this.store.frameSize.set(request);
//    allCallbacks.computeIfAbsent(FrameSize.class, k -> new ConcurrentLinkedQueue<>());
//    try {
//      allCallbacks.get(FrameSizeListener.class).forEach(c -> ((FrameSizeListener) c).onNewData(request));
//    } catch (Exception e) {
//      e.printStackTrace();
//      System.out.println("THIS CAUSES ME PHYSICAL PAIN, FIX YOUR CALLBACK");
//    }
//    responseObserver.onNext(reply);
//    responseObserver.onCompleted();
//  }
//
//
//  @Override
//  public void sendReflTape(ReflTapePair request, StreamObserver<Empty> responseObserver) {
//    Empty reply = Empty.newBuilder().build();
//    System.out.print(request);
//    this.store.reflTapeRef.set(request);
//    allCallbacks.computeIfAbsent(ReflTapePair.class, k -> new ConcurrentLinkedQueue<>());
//    try {
//      allCallbacks.get(ReflTapeListener.class).forEach(c -> ((ReflTapeListener) c).onNewData(request));
//    } catch (Exception e) {
//      e.printStackTrace();
//      System.out.println("GO REFLECT ON HOW YOUR CALLBACK SUCKS");
//    }
//    responseObserver.onNext(reply);
//    responseObserver.onCompleted();
//  }
//
//  @Override
//  public void sendGaffeTape(GaffeTape request, StreamObserver<Empty> responseObserver) {
//    Empty reply = Empty.newBuilder().build();
//    System.out.print(request);
//    this.store.gaffeRef.set(request);
//    allCallbacks.computeIfAbsent(GaffeTape.class, k -> new ConcurrentLinkedQueue<>());
//    try {
//      allCallbacks.get(GaffeListener.class).forEach(c -> ((GaffeListener) c).onNewData(request));
//    } catch (Exception e) {
//      e.printStackTrace();
//      System.out.println("YOUR CODE IS A GAFFE, FIX YOUR CALLBACK");
//    }
//    responseObserver.onNext(reply);
//    responseObserver.onCompleted();
//  }
}
