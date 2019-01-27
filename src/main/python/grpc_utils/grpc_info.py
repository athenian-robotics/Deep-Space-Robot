import grpc
import sys

# appended path so program can read the proto files
sys.path.append('build/generated/source/python')
import CVData_pb2_grpc

class GrpcInfo(object):
    def __init__(self, host='localhost', port=50051):
        self._host = host
        self._port = port

        self._channel = grpc.insecure_channel(self._host + ":" + str(self._port))
        self._stub = CVData_pb2_grpc.OpenCVInfoStub(self._channel)

    @property
    def stub(self):
        return self._stub


    def close(self):
        self._channel.close()
