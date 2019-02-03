from concurrent.futures import ThreadPoolExecutor

from grpc_utils.routeClient import *

# CONVENTION: INDEX STARTS AT 0

sftop = SharedFrame()
sfmed = SharedFrame()
sflow = SharedFrame()

topCamera = Camera(cameraIndex=0, shared_frame=sftop, resolution=(500, 500))
medCamera = Camera(cameraIndex=1, shared_frame=sfmed, resolution=(500, 500))
lowCamera = Camera(cameraIndex=2, shared_frame=sflow, resolution=(500, 500))

server = StreamServer(sftop, sfmed, sflow)

# TODO get rpc from button press

# TODO Implement Break Function

# placeholder host and port
grpc_client = RouteClient(host="localhost", port="5050")

with ThreadPoolExecutor() as executor:
    executor.submit(topCamera.start)
    executor.submit(medCamera.start)
    executor.submit(lowCamera.start)

    # stream started
    executor.submit(server.start)

    # alignment
    executor.submit(grpc_client.sendHatch, sfmed)
    executor.submit(grpc_client.sendBall, sfmed)

    # game object
    executor.submit(grpc_client.sendGaffeTape, sflow)
    executor.submit(grpc_client.sendReflTape, sftop)
