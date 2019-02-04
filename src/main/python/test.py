import numpy

from cv_utils.stream import *
from grpc_utils.CVObject import *

# range of values to scan
low = numpy.array([0, 230, 0])  # TODO find ideal value range
high = numpy.array([7, 255, 150])


def viewCargo(frame):
    # get the frame from the thread

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
        leftPoint = (0, 0)
        rightPoint = (0, 0)

    centroid = Point(int((leftPoint[0] + rightPoint[0]) / 2), int((leftPoint[1] + rightPoint[1]) / 2))
    diameter = rightPoint[0] - leftPoint[0]

    cv2.circle(frame, tuple((centroid.x, centroid.y)), 5, (255, 255, 255), 5)

    return cv2.bitwise_and(frame, frame, mask=contourmask)


cap = cv2.VideoCapture(1)
while True:
    ret, frame = cap.read()
    cv2.imshow("k", viewCargo(frame))
    cv2.waitKey(1)