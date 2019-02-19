from concurrent.futures import ThreadPoolExecutor
from threading import Thread

from cv_utils.viewthreads.overhead import *
from grpc_utils.routeClient import SharedFrame, Camera
from socket_server import SocketServer


# CONVENTION: INDEX STARTS AT 0
class StreamServer(object):
    # def __init__(self, sf0: SharedFrame, sf1: SharedFrame, sf2: SharedFrame):
    def __init__(self, sf0: SharedFrame, sf1: SharedFrame):
        self.sf0 = sf0
        self.sf1 = sf1

        # new common object for overlay
        self.sf2 = SharedFrame()

    def start(self):
        server = SocketServer()
        thread = Thread(target=server.run, args=[self.sf2, True])
        thread.start()

        while self.sf0.notComplete() and self.sf1.notComplete():
            # camera looking at reflective tape
            self.sf2.setFrame(viewReflTape(self.sf0.getFrame(), self.sf1.getFrame()))

"""
The Process:
    SharedFrame is the shared object, thread lock
    Camera starts the capture and sets the frame, producer
    
    ThreadPool -> camera0 starts capture
               -> camera1 starts capture
               
               -> socketServer start -> send driver and vision image -> overhead process image
               -> RouteClient -> package and send grpc values -> detectReflTape  
                
"""

HOSTNAME = "10.8.52.2"
PORT = "50051"


def main():
    sfdriver = SharedFrame()
    sfvision = SharedFrame()
    # sflow = SharedFrame()

    # 480 x 640 default
    driverCamera = Camera(cameraIndex=0, shared_frame=sfdriver, resolution=(400, 400))
    visionCamera = Camera(cameraIndex=1, shared_frame=sfvision, resolution=(400, 400))
    # lowCamera = Camera(cameraIndex=2, shared_fyrame=sflow, resolution=(300, 300))

    # grpc_client = RouteClient(host=HOSTNAME, port=PORT)
    socketServer = StreamServer(sfdriver, sfvision)

    with ThreadPoolExecutor() as executor:
        try:
            executor.submit(driverCamera.start)
            executor.submit(visionCamera.start)

            # executor.submit(grpc_client.sendCamPose,sftop)
            socketServer.start()

        except KeyboardInterrupt:
            sfvision.markCompleted()
            sfdriver.markCompleted()


if __name__ == '__main__':
    main()
