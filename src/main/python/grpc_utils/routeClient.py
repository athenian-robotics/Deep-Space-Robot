import sys

from grpc_utils import grpc_info
from grpc_utils.CVObject import *

# appended path so program can read the proto files
sys.path.append('build/generated/source/python')
import CVData_pb2

class RouteClient:

    def __init__(self, host: str, port):
        self.grpcInfo = grpc_info.GrpcInfo(host=host, port=port)

    def sendFrameSize(self, width: int, height: int):
        data = CVData_pb2.FrameSize(x=width, y=height)
        response = self.grpcInfo.stub.SendFrameSize(data)
        if response:
            print("Data sent!")
            print(data)

    def sendCVData(self, CVBulk: CVData):
        def newPoint(p: Point):
            return CVData_pb2.Point(x=p.x, y=p.y)

        # TODO Implement to string function
        def toString(bulk: CVData) -> str:
            return bulk

        glt = CVData_pb2.ReflTape(degrees=CVBulk.leftTape.degree,
                                       size=CVBulk.leftTape.size,
                                       topInside=newPoint(CVBulk.leftTape.topInside),
                                       centroid=newPoint(CVBulk.leftTape.centroid),
                                       bottomOutside=newPoint(CVBulk.leftTape.bottomOutside))

        grt = CVData_pb2.ReflTape(degrees=CVBulk.rightTape.degree,
                                       size=CVBulk.rightTape.size,
                                       topInside=newPoint(CVBulk.rightTape.topInside),
                                       centroid=newPoint(CVBulk.rightTape.centroid),
                                       bottomOutside=newPoint(CVBulk.rightTape.bottomOutside))

        ggt = CVData_pb2.GaffeTape(degrees=CVBulk.gaffeTape.degree,
                                        front=newPoint(CVBulk.gaffeTape.front),
                                        centroid=newPoint(CVBulk.gaffeTape.centroid),
                                        back=newPoint(CVBulk.gaffeTape.back))

        data = CVData_pb2.CVData(left=glt, right=grt, tape=ggt)
        response = self.grpcInfo.stub.SendCVData(data)
        if response:
            print("Data sent!")

    def sendBall(self, ball: Ball):
        def newPoint(p: Point):
            return CVData_pb2.Point(x=p.x, y=p.y)

        def toString(ball: Ball) -> str:
            return ball

        radius = ball.radius
        centroid = newPoint(ball.centroid)
        obj = CVData_pb2.GameObject(radius=radius, centroid=centroid)
        response = self.grpcInfo.stub.SendBall(obj)
        if response:
            print("Data sent!")

    def sendHatch(self, hatch: Hatch):
        def newPoint(p: Point):
            return CVData_pb2.Point(x=p.x, y=p.y)

        def toString(hatch: Hatch) -> str:
            return hatch

        radius = hatch.radius
        centroid = newPoint(hatch.centroid)
        obj = CVData_pb2.GameObject(radius=radius, centroid=centroid)
        response = self.grpcInfo.stub.SendHatch(obj)
        if response:
            print("Data sent!")
