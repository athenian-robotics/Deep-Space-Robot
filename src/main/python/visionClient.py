from concurrent.futures import ThreadPoolExecutor

from image_server import ImageServer
from socket_server import SocketServer

from cv_utils.viewthreads.viewReflTape import *
from grpc_utils.routeClient import SharedFrame, Camera, RouteClient


# CONVENTION: INDEX STARTS AT 0
class StreamServer(object):
    # def __init__(self, sf0: SharedFrame, sf1: SharedFrame, sf2: SharedFrame):
    def __init__(self, sf0: SharedFrame, sf1: SharedFrame):
        self.sf0 = sf0
        self.sf1 = sf1
        # self.sf2 = sf2

    def start(self):
        server = SocketServer()
        with ThreadPoolExecutor() as executor:
            executor.submit(server.run, self.sf0, isSharedFrame=True)

        # while self.sf0.notComplete() and self.sf1.notComplete() and self.sf2.notComplete():
        while self.sf0.notComplete() and self.sf1.notComplete():

            # camera looking at reflective tape
            # rftAssist = viewReflTape(self.sf0.getFrame())
            rftAssist = viewReflTape(self.sf1.getFrame())

            # camera for driver assist only?
            # medStream = self.sf1.getFrame()
            medStream = self.sf0.getFrame()

            # camera looking at tape for auto alignment
            #lowStream = self.sf2.getFrame()
            # lowStream = self.sf0.getFrame()

            # server.image(rftAssist, "cam0")
            # server.image(medStream, "cam1")
            #server.image(lowStream, "cam2")


"""
The Process:
    SharedFrame is the shared object, thread lock
    Camera starts the capture and sets the frame, producer
    StreamServer connects the image pipe to the http server, consumer
    
    ThreadPool -> camera0 starts capture
               -> camera1 starts capture
               -> camera2 starts capture
               
               -> http server starts -> send processed image for driver assist camera0 -> viewReflTape
                                     -> send unprocessed image from camera1
                                     -> send unprocessed image from camera2
               
               RouteClient -> package and send grpc values -> detectReflTape  
                
"""

# TODO Actual Host
# HOSTNAME = "localhost"
# PORT = "5050"

# TODO get roborio
HOSTNAME = "10.8.52.2"
PORT = "50051"


# HELLO BARE REPO

def main():
    sftop = SharedFrame()
    sfmed = SharedFrame()
    #sflow = SharedFrame()

    # 480 x 640 default
    topCamera = Camera(cameraIndex=0, shared_frame=sftop, resolution=(200, 200))
    medCamera = Camera(cameraIndex=1, shared_frame=sfmed, resolution=(200, 200))
    #lowCamera = Camera(cameraIndex=2, shared_frame=sflow, resolution=(300, 300))

    grpc_client = RouteClient(host=HOSTNAME, port=PORT)
    # httpserver = StreamServer(sftop, sfmed, sflow)
    httpserver = StreamServer(sftop, sfmed)

    with ThreadPoolExecutor() as executor:
        executor.submit(topCamera.start)
        executor.submit(medCamera.start)
        # executor.submit(lowCamera.start)

        # executor.submit(grpc_client.sendCamPose,sftop)

        # alignment
        # executor.submit(grpc_client.sendHatch, sfmed)
        # executor.submit(grpc_client.sendBall, sfmed)

        # stream started6
        # executor.submit(grpc_client.sendGaffeTape, sflow)
        # executor.submit(grpc_client.sendReflTape, sftop)

        httpserver.start()


if __name__ == '__main__':
    main()
