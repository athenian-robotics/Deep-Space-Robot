import numpy

from cv_utils.stream import *
from grpc_utils.CVObject import *

# range of values to scan
low = numpy.array([0, 100, 40])  # TODO find ideal value range
high = numpy.array([180, 190, 100])


def viewGaffeTape(frame):
    blurredframe = cv2.blur(frame, (5, 5))  # blur image
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)  # change colorspace to HSV
    colormask = cv2.inRange(hsv, low, high)  # find tape
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE,
                                                        cv2.CHAIN_APPROX_SIMPLE)  # create a list of contours

    wasDetected = False
    if len(contours) > 0:
        ordered = sorted(contours, key=cv2.contourArea, reverse=True)  # arrange contours by area

        contour = ordered[0]  # biggest contour
        bottompt = sorted(contour, key=lambda a: a[0][1])[0][
            0]  # find the point with the smallest y value in the contour
        bottomobj = Point(bottompt[0], bottompt[1])  # create Point object ^^^
        toppt = sorted(contour, key=lambda a: a[0][1])[-1][0]  # find the point with the largest y value in the contour
        topobj = Point(toppt[0], toppt[1])  # create Point object ^^^
        centroid = Point(int((bottompt[0] + toppt[0]) / 2),
                         int((bottompt[1] + toppt[1]) / 2))  # find centroid as the average of ^^^
        if len(contour) > 5:
            (x1, y1), (MA1, ma1), angle = cv2.fitEllipse(
                contour)  # if there are enough points, find the angle of the contour (using fitEllipse)
        else:
            angle = 0  # default
        wasDetected = True

        cv2.drawContours(frame, contour, -1, (255, 0, 0), 4)
        cv2.circle(frame, (centroid.x, centroid.y), 7, (0, 255, 0), 8)

    return frame


cap = cv2.VideoCapture(1)

while True:
    ret, frame = cap.read()
    cv2.imshow("k", viewGaffeTape(frame))
    cv2.waitKey(1)
