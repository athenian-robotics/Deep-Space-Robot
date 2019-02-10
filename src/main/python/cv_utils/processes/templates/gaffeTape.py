import numpy

from cv_utils.stream import *
from grpc_utils.CVObject import *

# range of values to scan
low = numpy.array([0, 100, 40])
high = numpy.array([180, 190, 100])

def detectGaffeTape(shared_frame: SharedFrame):
    frame = shared_frame.getFrame()
    # blur image
    blurredframe = cv2.blur(frame, (5, 5))
    # change colorspace to HSV
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)
    # find tape
    colormask = cv2.inRange(hsv, low, high)
    # create a list of contours
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)

    if len(contours) > 0:
        ordered = sorted(contours, key=cv2.contourArea, reverse=True)  # arrange contours by area
        # biggest contour
        contour = ordered[0]
        # find the point with the smallest y value in the contour
        bottompt = sorted(contour, key=lambda a: a[0][1])[0][0]
        # create Point object ^^^
        bottomobj = Point(bottompt[0], bottompt[1])
        # find the point with the largest y value in the contour
        toppt = sorted(contour, key=lambda a: a[0][1])[-1][0]
        # create Point object ^^^
        topobj = Point(toppt[0], toppt[1])
        # find centroid as the average of ^^^
        centroid = Point(int((bottompt[0] + toppt[0]) / 2), int((bottompt[1] + toppt[1]) / 2))
        if len(contour) > 5:
            (x1, y1), (MA1, ma1), angle = cv2.fitEllipse(contour)
        else:
            angle = 0  # default

        return GaffeTape(angle, topobj, bottomobj, centroid)  # jam all values into a gaffetape object
    else:
        return GaffeTape(0, Point(0,0), Point(0,0), Point(0,0))