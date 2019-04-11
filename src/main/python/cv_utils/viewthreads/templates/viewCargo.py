import numpy

from cv_utils.stream import *
from grpc_utils.CVObject import *

# range of values to scan
low = numpy.array([0, 230, 0])
high = numpy.array([7, 255, 150])


def viewCargo(shared_frame: SharedFrame):
    # get the frame from the thread
    frame = shared_frame.getFrame()

    blurredframe = cv2.blur(frame, (5, 5))  # blur image
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)  # change colorspace to HSV
    colormask = cv2.inRange(hsv, low, high)  # find tape
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE,
                                                        cv2.CHAIN_APPROX_SIMPLE)  # create a list of contours

    if len(contours) > 0:
        ordered = sorted(contours, key=cv2.contourArea, reverse=True)  # arrange contours by area
        points = sorted(ordered[0], key=lambda a: a[0][0])  # pick largest contour and make a sorted list of points
        leftPoint = points[0][0]  # choose the point with the lowest x value
        rightPoint = points[-1][0]  # choose the point with the highest x value
        wasDetected = True
    else:
        wasDetected = False
        leftPoint = 0
        rightPoint = 0

    centroid = Point((leftPoint[0] + rightPoint[0]) / 2, (leftPoint[1] + rightPoint[2]) / 2)
    diameter = rightPoint[0] - leftPoint[0]

    return Ball(wasDetected, centroid, diameter)
