import sys

sys.path.append("src/main/python")
from cv_utils.processes.detectReflTape import *
from cv_utils.stream import *
from grpc_utils import grpc_info

# appended path so program can read the proto files
sys.path.append('build/generated/source/python')
import CVData_pb2

class RouteClient:
    def __init__(self, host: str, port):
        self.grpcInfo = grpc_info.GrpcInfo(host=host, port=port)

    def sendCamPose(self, share_frame: SharedFrame):
        while share_frame.notComplete():
            vectorPair = detectReflTape(share_frame.getFrame())

            gtsv = CVData_pb2.Vector3D(x=vectorPair.translation.x,
                                       y=vectorPair.translation.y,
                                       z=vectorPair.translation.z)

            grtv = CVData_pb2.Vector3D(x=vectorPair.rotational.x,
                                       y=vectorPair.rotational.y,
                                       z=vectorPair.rotational.z)

            cameraPose = CVData_pb2.CameraPose(translation=gtsv,
                                               rotation=grtv)

            self.grpcInfo.stub.SendCamerPose(cameraPose)

    # def sendReflTape(self, shared_frame: SharedFrame):
    #     while shared_frame.notComplete():
    #         tapePairs = detectReflTape(shared_frame.getFrame())
    #         glt = CVData_pb2.ReflTape(blobsize=tapePairs.leftTape.blobsize)
    #         grt = CVData_pb2.ReflTape(blobsize=tapePairs.rightTape.blobsize)
    #
    #         # Tape Pairs
    #         pair = CVData_pb2.ReflTapePair(leftTape=glt,
    #                                        rightTape=grt,
    #                                        centroid=tapePairs.centroid,
    #                                        distance=tapePairs.distance)
    #         self.grpcInfo.stub.SendReflTape(pair)
    #
    # def newPoint(self, p: Point):
    #     return CVData_pb2.Point(x=p.x, y=p.y)
    #
    # def sendFrameSize(self, width: int, height: int):
    #     data = CVData_pb2.FrameSize(x=width, y=height)
    #     self.grpcInfo.stub.SendFrameSize(data)
    #
    #
    # def sendGaffeTape(self, shared_frame: SharedFrame):
    #     while True:
    #         gaffeTape = detectGaffeTape(shared_frame.getFrame())
    #         ggt = CVData_pb2.GaffeTape(degrees=gaffeTape.degree,
    #                                    front=self.newPoint(gaffeTape.front),
    #                                    centroid=self.newPoint(gaffeTape.centroid),
    #                                    back=self.newPoint(gaffeTape.back))
    #         self.grpcInfo.stub.SendCVData(ggt)
    #
    # def sendBall(self, shared_frame: SharedFrame):
    #     while True:
    #         ball = detectCargo(shared_frame.getFrame())
    #         gB = CVData_pb2.Ball(centroid=self.newPoint(ball.centroid),
    #                              diameter=ball.diameter)
    #
    #         self.grpcInfo.stub.SendBall(gB)
    #
    # def sendHatch(self, shared_frame: SharedFrame):
    #     while True:
    #         hatch = detectHatchPanel(shared_frame.getFrame())
    #         gH = CVData_pb2.Hatch(centroid=self.newPoint(hatch.centroid),
    #                               diameter=hatch.diameter)
    #
    #         self.grpcInfo.stub.SendHatch(gH)
