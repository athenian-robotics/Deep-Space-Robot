import sys

from cv_utils.processes.detectCargo import *
from cv_utils.processes.detectGaffeTape import *
from cv_utils.processes.detectHatchPanel import *
from cv_utils.processes.detectReflTape import *
from cv_utils.stream import *
from grpc_utils import grpc_info
from grpc_utils.CVObject import *

# appended path so program can read the proto files
sys.path.append('build/generated/source/python')
import CVData_pb2

class RouteClient:

    def __init__(self, host: str, port):
        self.grpcInfo = grpc_info.GrpcInfo(host=host, port=port)

    def newPoint(self, p: Point):
        return CVData_pb2.Point(x=p.x, y=p.y)

    def sendFrameSize(self, width: int, height: int):
        data = CVData_pb2.FrameSize(x=width, y=height)
        self.grpcInfo.stub.SendFrameSize(data)

    def sendReflTape(self, shared_frame: SharedFrame):
        while True:
            ReflTape = detectReflTape(shared_frame.getFrame())
            glt = CVData_pb2.ReflTape(degrees=ReflTape.leftTape.degree,
                                      size=ReflTape.leftTape.size,
                                      topInside=self.newPoint(ReflTape.leftTape.topInside),
                                      centroid=self.newPoint(ReflTape.leftTape.centroid),
                                      bottomOutside=self.newPoint(ReflTape.leftTape.bottomOutside))

            grt = CVData_pb2.ReflTape(degrees=ReflTape.rightTape.degree,
                                      size=ReflTape.rightTape.size,
                                      topInside=self.newPoint(ReflTape.rightTape.topInside),
                                      centroid=self.newPoint(ReflTape.rightTape.centroid),
                                      bottomOutside=self.newPoint(ReflTape.rightTape.bottomOutside))

            pair = CVData_pb2.ReflTapePair(leftTape=glt, rightTape=grt)
            self.grpcInfo.stub.SendReflTape(pair)

    def sendGaffeTape(self, shared_frame: SharedFrame):
        while True:
            gaffeTape = detectGaffeTape(shared_frame.getFrame())
            ggt = CVData_pb2.GaffeTape(degrees=gaffeTape.degree,
                                       front=self.newPoint(gaffeTape.front),
                                       centroid=self.newPoint(gaffeTape.centroid),
                                       back=self.newPoint(gaffeTape.back))
            self.grpcInfo.stub.SendCVData(ggt)

    def sendBall(self, shared_frame: SharedFrame):
        while True:
            ball = detectCargo(shared_frame.getFrame())
            gB = CVData_pb2.Ball(centroid=self.newPoint(ball.centroid),
                                 diameter=ball.diameter)

            self.grpcInfo.stub.SendBall(gB)

    def sendHatch(self, shared_frame: SharedFrame):
        while True:
            hatch = detectHatchPanel(shared_frame.getFrame())
            gH = CVData_pb2.Hatch(centroid=self.newPoint(hatch.centroid),
                                  diameter=hatch.diameter)

            self.grpcInfo.stub.SendHatch(gH)
