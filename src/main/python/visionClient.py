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
        self.sf2 = SharedFrame()

    def start(self):
        server = SocketServer()
        thread = Thread(target=server.run, args=[self.sf2, True])
        thread.start()

        while self.sf0.notComplete() and self.sf1.notComplete():

            # camera looking at reflective tape
            # rftAssist = viewReflTape(self.sf0.getFrame())
            self.sf2.setFrame(viewReflTape(self.sf0.getFrame(), self.sf1.getFrame()))
            # self.sf2.setFrame(viewReflTape(self.sf1.getFrame()), self.sf0.getFrame()))

            # camera for driver assist only?
            # medStream = self.sf1.getFrame()
            # medStream = self.sf0.getFrame()

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


def main():
    sfdriver = SharedFrame()
    sfvision = SharedFrame()
    #sflow = SharedFrame()

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
