import logging
import sys

# appended path so program can read the proto files

sys.path.append('build/generated/source/python')


import CVData_pb2
import grpc_info
from CVObject import *
from enum import Enum


class RouteClient:
    class ClientTypes(Enum):
        CVData = "sendCVData"
        FrameData = "sendFrameSize"

    @staticmethod
    def sendFrameSize(grpcInfo, width: int, height: int):
        response = grpcInfo.stub.SendFrameSize(x=width, y=height)
        print("frame size set to: {}".format(response.x, response.y))

    @staticmethod
    def sendCVData(grpcInfo, CVBulk: CVData):
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

        grpcInfo.stub.SendCVData(left=glt, right=grt, tape=ggt)
        print(toString(CVBulk))

    @staticmethod
    def main(typecheck: ClientTypes, kwargs):
        logger = logging.getLogger(__name__)
        try:
            with grpc_info.GrpcInfo(host='localhost:', port=50051) as grpcInfo:
                if len(kwargs) == 2:
                    eval(typecheck.value)(grpcInfo, kwargs.get("width"), kwargs.get("height"))
                elif len(kwargs) == 1:
                    eval(typecheck.value)(grpcInfo, kwargs.get("CVBulk"))

        except BaseException as e:
            logger.error("Failure with: [{0}]".format(e))
            return

    # Type is CVData and FrameData
    @staticmethod
    def sendInfo(typecheck: ClientTypes = None, **kwargs):
        assert typecheck is not None
        logging.basicConfig()
        RouteClient.main(typecheck, kwargs)
