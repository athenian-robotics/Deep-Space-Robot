from concurrent.futures import ThreadPoolExecutor

from cv_utils.stream import *


def test(shared_frame1: SharedFrame, shared_frame2: SharedFrame, shared_frame3: SharedFrame):
    server = ImageServer('multi-image.html')
    server.start()

    while shared_frame1.notComplete() and shared_frame2.notComplete():
        server.image(shared_frame1.getFrame(), "cam1")
        server.image(shared_frame2.getFrame(), "cam2")
        server.image(shared_frame3.getFrame(), "cam3")


# CONVENTION: INDEX STARTS AT 0

sf0 = SharedFrame()
sf1 = SharedFrame()
sf2 = SharedFrame()

camera0 = Camera(cameraIndex=0, shared_frame=sf0, resolution=(300, 300))
camera1 = Camera(cameraIndex=1, shared_frame=sf1, resolution=(300, 300))
camera2 = Camera(cameraIndex=2, shared_frame=sf2, resolution=(300, 300))

server = StreamServer(sf0, sf1, sf2)
# TODO Implement Processes

# TODO Implement Frame Executor


with ThreadPoolExecutor() as executor:
    executor.submit(camera0.start)
    executor.submit(camera1.start)
    executor.submit(camera2.start)

    executor.submit(server.start)
