from concurrent.futures import ThreadPoolExecutor

from arc852.image_server import ImageServer

from sample.camera import *


def test(shared_frame: SharedFrame):
    server = ImageServer('webLoader.html')
    server.start()

    while shared_frame.notComplete:
        data = shared_frame.getFrame()
        server.image = data


sf1 = SharedFrame()
sf2 = SharedFrame()
sf3 = SharedFrame()

camera1 = Camera(cameraIndex=0, shared_frame=sf1)
camera2 = Camera(cameraIndex=1, shared_frame=sf2)
camera3 = Camera(cameraIndex=2, shared_frame=sf3)

# TODO Implement Processes
# TODO Implement Frame Executor
# TODO Implement Ending

with ThreadPoolExecutor() as executor:
    executor.submit(camera1.start)
    executor.submit(test, sf1, )
