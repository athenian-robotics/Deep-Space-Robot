import cv2
import io
import socket
import struct
import time
import pickle
import zlib

HOST = '0.0.0.0'
PORT = 8081

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')

server_socket.bind((HOST, PORT))
print('Socket bind complete')
server_socket.listen(10)
print('Socket now listening')

conn, addr = server_socket.accept()

cam = cv2.VideoCapture(0)

cam.set(3, 640)
cam.set(4, 480)

img_counter = 0

encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]

while True:
    ret, frame = cam.read()
    result, frame = cv2.imencode('.jpg', frame, encode_param)
    #    data = zlib.compress(pickle.dumps(frame, 0))
    data = pickle.dumps(frame, 0)
    size = len(data)

    print("{}: {}".format(img_counter, size))
    conn.sendall(struct.pack(">L", size) + data)
    img_counter += 1

cam.release()
