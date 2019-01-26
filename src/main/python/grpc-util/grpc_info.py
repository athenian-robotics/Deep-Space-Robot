import grpc
import CVData_pb2_grpc


class GrpcInfo(object):
    def __init__(self, host='localhost', port=50051):
        self._host = host
        self._port = port

    @property
    def stub(self):
        return self._stub

    def __enter__(self):
        self._channel = grpc.insecure_channel(self._host + ":" + str(self._port))
        self._stub = CVData_pb2_grpc.OpenCVInfoStub(self._channel)
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self._channel.close()
        if exc_val is not None:
            raise exc_val
        return self
