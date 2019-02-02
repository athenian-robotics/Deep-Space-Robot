from concurrent.futures import ThreadPoolExecutor

from cv_utils.stream import *

# CONVENTION: INDEX STARTS AT 0

sft = SharedFrame()
sfm = SharedFrame()
sfl = SharedFrame()

topCamera = Camera(cameraIndex=0, shared_frame=sft, resolution=(500, 500))
medCamera = Camera(cameraIndex=1, shared_frame=sfm, resolution=(500, 500))
lowCamera = Camera(cameraIndex=2, shared_frame=sfl, resolution=(500, 500))

server = StreamServer(sft, sfm, sfl)

# TODO Implement Processes
# TODO get rpc from button press


# TODO Implement Frame Executor


# TODO Implement Break Function


with ThreadPoolExecutor() as executor:
    executor.submit(topCamera.start)
    executor.submit(medCamera.start)
    executor.submit(lowCamera.start)

    # stream started
    executor.submit(server.start)

    executor.submit()
    # processes started
