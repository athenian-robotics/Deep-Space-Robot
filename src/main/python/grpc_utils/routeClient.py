import logging
import sys

from CVObject import *
from enum import Enum
import grpc_info

# appended path so program can read the proto files
sys.path.append('build/generated/source/python')
import CVData_pb2

class RouteClient:

    def __init__(self, host: str, port):
        self.grpcInfo = grpc_info.GrpcInfo(host=host, port=port)

    def sendFrameSize(self, width: int, height: int):
        data = CVData_pb2.FrameSize(x=width, y=height)
        response = self.grpcInfo.stub.SendFrameSize(data)

    def sendCVData(self, CVBulk: CVData):
        def newPoint(p: Point):
            return CVData_pb2.Point(x=p.x, y=p.y)

        # TODO Implement to string function
        def toString(bulk: CVData) -> str:
            return "to be implemented..."

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
        self.grpcInfo.stub.SendCVData(data)
        print(toString(CVBulk))

k = RouteClient("localhost", 50051)
k.sendFrameSize(width=1, height=2)
k.sendCVData(CVData(ReflectiveTape(1,2,Point(3,4),Point(5,6), Point(7,8)), ReflectiveTape(9,10,Point(11,12),Point(13,14), Point(15,16)), GaffeTape(17, Point(18,19), Point(20,21), Point(22,23))))
k.grpcInfo.close()