package frc.team852.lib.gRPC;

import com.google.common.collect.Lists;
import com.google.protobuf.Empty;
import frc.team852.DeepSpaceRobot.CVData;
import frc.team852.DeepSpaceRobot.FrameSize;
import frc.team852.DeepSpaceRobot.OpenCVInfoGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.stream.IntStream;

class CVDataImpl extends OpenCVInfoGrpc.OpenCVInfoImplBase {

    @Override
    public void setFrameSize (FrameSize request, StreamObserver<Empty> responseObserver) {
        Empty reply = Empty.newBuilder().build();
        System.out.print(request);
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void sendCVData (CVData request, StreamObserver<Empty> responseObserver) {
        Empty reply = Empty.newBuilder().build();
        System.out.print(request);
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
