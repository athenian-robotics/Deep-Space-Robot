import traceback

import cv2
import io
import socket
import struct
import time
import pickle
import zlib


class SocketServer:
    def __init__(self, host='0.0.0.0', port=8081):
        self.host = host
        self.port = port
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print('Socket created')
        self.server_socket.bind((self.host, self.port))
        print('Socket bind complete')
        self.server_socket.listen(10)
        print('Socket now listening')
        self.conn, self.addr = self.server_socket.accept()
        self.encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]

    def run(self, cam, is_shared_frame=False):
        img_counter = 0
        while True:
            try:
                if is_shared_frame:
                    frame = cam.getFrame()
                else:
                    ret, frame = cam.read()
                result, frame = cv2.imencode('.jpg', frame, self.encode_param)
                #    data = zlib.compress(pickle.dumps(frame, 0))
                data = pickle.dumps(frame, 0)
                size = len(data)

                print("{}: {}".format(img_counter, size))
                self.conn.sendall(struct.pack(">L", size) + data)

                img_counter += 1
            except KeyboardInterrupt:
                return

            except BaseException:
                traceback.print_exc()

        cam.release()


if __name__ == "__main__":
    s = SocketServer()
    s.run(cv2.VideoCapture(0))
