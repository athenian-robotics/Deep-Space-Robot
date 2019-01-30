from concurrent.futures import ThreadPoolExecutor

from arc852.image_server import ImageServer

from sample.camera import *

# TODO implement multi-threading


shared_frame = SharedFrame()


def test(shared_frame: SharedFrame):
    server = ImageServer('webLoader.html')
    server.start()

    while shared_frame.notComplete:
        data = shared_frame.getFrame()
        server.image = data


camera1 = Camera(cameraIndex=0, shared_frame=shared_frame)

with ThreadPoolExecutor() as executor:
    executor.submit(camera1.start)
    executor.submit(test, shared_frame, )
