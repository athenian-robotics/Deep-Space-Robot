from socket_server import SocketServer
import cv2

cam = cv2.VideoCapture(0)
cam.set(3, 200)
cam.set(4, 200)
server = SocketServer()
server.run(cam, is_shared_frame=False)
