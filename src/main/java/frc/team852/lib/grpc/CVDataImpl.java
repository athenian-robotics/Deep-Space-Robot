package frc.team852.lib.grpc;

import com.google.protobuf.Empty;
import frc.team852.DeepSpaceRobot.*;
import frc.team852.Robot;
import frc.team852.lib.CVDataStore;
import frc.team852.lib.callbacks.BallListener;
import frc.team852.lib.callbacks.FrameSizeListener;
import frc.team852.lib.callbacks.HatchListener;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

class CVDataImpl extends OpenCVInfoGrpc.OpenCVInfoImplBase {
  CVDataStore store = Robot.dataStore;
  private List<BallListener> ballCallbacks = new ArrayList<>();
  private List<HatchListener> hatchCallbacks = new ArrayList<>();
  private List<FrameSizeListener> frameSizeCallbacks = new ArrayList<>();

  @Override
  public void sendBall(Ball request, StreamObserver<Empty> responseObserver) {
    Empty reply = Empty.newBuilder().build();
    System.out.print(request);
    this.store.ballRef.set(request);
    try {
      ballCallbacks.forEach(bc -> bc.onNewData(request));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("FIX YOUR CALLBACK YOU LOUT");
    }
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  @Override
  public void sendHatch(Hatch request, StreamObserver<Empty> responseObserver) {
    Empty reply = Empty.newBuilder().build();
    System.out.print(request);
    this.store.hatchRef.set(request);
    try {
      hatchCallbacks.forEach(hc -> hc.onNewData(request));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("HEY BURRITO BRAINS, FIX YOUR CALLBACK");
    }
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  @Override
  public void sendFrameSize(FrameSize request, StreamObserver<Empty> responseObserver) {
    Empty reply = Empty.newBuilder().build();
    System.out.print(request);
    this.store.frameSize.set(request);
    try {
      frameSizeCallbacks.forEach(fsc -> fsc.onNewData(request));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("THIS CAUSES ME PHYSICAL PAIN, FIX YOUR CALLBACK");
    }
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  @Override
  public void sendCVData(CVData request, StreamObserver<Empty> responseObserver) {
    Empty reply = Empty.newBuilder().build();
    System.out.print(request);
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  public void registerBallListener(BallListener listener) {
    this.ballCallbacks.add(listener);
  }

  public void registerHatchListener(HatchListener listener) {
    this.hatchCallbacks.add(listener);
  }


}
