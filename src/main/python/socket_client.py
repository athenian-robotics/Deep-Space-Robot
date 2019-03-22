import pickle
import socket
import struct  ## new
import traceback

import cv2


class SocketClient:
    def __init__(self):
        # HOST = '0.0.0.0'
        HOST = '10.16.103.41'
        PORT = 8081

        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client_socket.connect((HOST, PORT))
        self.connection = self.client_socket.makefile('wb')

        self.data = b""
        self.payload_size = struct.calcsize(">L")

    def run(self):
        while True:
            try:
                while len(self.data) < self.payload_size:
                    self.data += self.client_socket.recv(4096)

                packed_msg_size = self.data[:self.payload_size]
                self.data = self.data[self.payload_size:]

                msg_size = struct.unpack(">L", packed_msg_size)[0]

                while len(self.data) < msg_size:
                    self.data += self.client_socket.recv(4096)
                frame_data = self.data[:msg_size]
                self.data = self.data[msg_size:]

                frame = pickle.loads(frame_data, fix_imports=True, encoding="bytes")
                frame = cv2.imdecode(frame, cv2.IMREAD_COLOR)
                cv2.imshow('ImageWindow', frame)
                cv2.waitKey(1)
            except Exception as e:
                traceback.print_exc()


if __name__ == '__main__':
    client = SocketClient()
    client.run()
