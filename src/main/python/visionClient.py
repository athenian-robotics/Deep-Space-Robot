from concurrent.futures import ThreadPoolExecutor
from arc852.multi_image_server import ImageServer
from sample.camera import *


def test(shared_frame1: SharedFrame, shared_frame2: SharedFrame, shared_frame3: SharedFrame):
    server = ImageServer('multi-image.html')
    server.start()

    while shared_frame1.notComplete() and shared_frame2.notComplete():
        server.image(shared_frame1.getFrame(), "cam1")
        server.image(shared_frame2.getFrame(), "cam2")
        server.image(shared_frame3.getFrame(), "cam3")

sf1 = SharedFrame()
sf2 = SharedFrame()
sf3 = SharedFrame()

camera1 = Camera(cameraIndex=2, shared_frame=sf1, resolution=(300,300))
camera2 = Camera(cameraIndex=1, shared_frame=sf2, resolution=(300,300))
camera3 = Camera(cameraIndex=0, shared_frame=sf3, resolution=(300,300))


# TODO Implement Processes
# TODO Implement Frame Executor

with ThreadPoolExecutor() as executor:
    executor.submit(camera1.start)
    executor.submit(camera2.start)
    executor.submit(camera3.start)
    executor.submit(test, sf1, sf2, sf3)
