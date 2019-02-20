# import socket
# import sys
# import cv2
# import pickle
# import numpy as np
# import struct  ## new
# import zlib
# import traceback
#
#
# HOST = '0.0.0.0'
# #HOST = '10.8.52.35'
# PORT = 8081
#
# self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# self.client_socket.connect((HOST, PORT))
# self.connection = self.client_socket.makefile('wb')
#
# self.data = b""
# self.payload_size = struct.calcsize(">L")
# print("self.payload_size: {}".format(self.payload_size))
#
#
# while True:
#     try:
#         while len(self.data) < self.payload_size:
#             print("Recv: {}".format(len(self.data)))
#             self.data += self.client_socket.recv(4096)
#
#         print("Done Recv: {}".format(len(self.data)))
#
#         packed_msg_size = self.data[:self.payload_size]
#         self.data = self.data[self.payload_size:]
#
#         msg_size = struct.unpack(">L", packed_msg_size)[0]
#         print("msg_size: {}".format(msg_size))
#
#         while len(self.data) < msg_size:
#             self.data += self.client_socket.recv(4096)
#         frame_self.data = self.data[:msg_size]
#         self.data = self.data[msg_size:]
#
#         frame = pickle.loads(frame_self.data, fix_imports=True, encoding="bytes")
#         frame = cv2.imdecode(frame, cv2.IMREAD_COLOR)
#         cv2.imshow('ImageWindow', frame)
#         cv2.waitKey(1)
#     except Exception as e:
#         traceback.print_exc()

import pickle
import socket
import struct  ## new
import traceback

import cv2


class SocketClient:
    def __init__(self):
        HOST = '0.0.0.0'
        # HOST = '10.8.52.35'
        PORT = 8081

        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client_socket.connect((HOST, PORT))
        self.connection = self.client_socket.makefile('wb')

        self.data = b""
        self.payload_size = struct.calcsize(">L")
        # print("self.payload_size: {}".format(self.payload_size))

    def run(self):
        while True:
            try:
                while len(self.data) < self.payload_size:
                    print("Recv: {}".format(len(self.data)))
                    self.data += self.client_socket.recv(4096)

                print("Done Recv: {}".format(len(self.data)))

                packed_msg_size = self.data[:self.payload_size]
                self.data = self.data[self.payload_size:]

                msg_size = struct.unpack(">L", packed_msg_size)[0]
                print("msg_size: {}".format(msg_size))

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
