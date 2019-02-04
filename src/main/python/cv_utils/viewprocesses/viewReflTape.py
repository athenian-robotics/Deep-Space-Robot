import numpy
5
from cv_utils.stream import *
from grpc_utils.CVObject import *

# range of values to scan
low = numpy.array([80, 50, 200])  # TODO find ideal value range
high = numpy.array([100, 230, 255])


def viewReflTape(shared_frame: SharedFrame):
    frame = shared_frame.getFrame()
    blurredframe = cv2.blur(frame, (5, 5))  # blur image
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)  # change colorspace to HSV
    colormask = cv2.inRange(hsv, low, high)  # find tape
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE,
                                                        cv2.CHAIN_APPROX_SIMPLE)  # create a list of contours
    wasDetected1, wasDetected2 = False, False
    if len(contours) > 0:
        ordered = sorted(contours, key=cv2.contourArea, reverse=True)  # arrange contours by area
        if len(ordered) > 1:
            wasDetected1 = True
            contour1 = ordered[0]  # biggest contour
            leftpt1 = sorted(contour1, key=lambda a: a[0][0])[0][0]  # find the point with the smallest x value in the contour
            leftobj1 = Point(leftpt1[0], leftpt1[1])  # create Point object ^^^
            rightpt1 = sorted(contour1, key=lambda a: a[0][0])[-1][0]  # find the point with the largest x value in the contour
            rightobj1 = Point(rightpt1[0], rightpt1[1])  # create Point object ^^^
            centroid1 = (int((leftpt1[0] + rightpt1[0]) / 2), int((leftpt1[1] + rightpt1[1]) / 2))  # find centroid as the average of ^^^
            area1 = cv2.contourArea(contour1)  # find area of contour
            if len(contour1) > 5:
                (x1, y1), (MA1, ma1), angle1 = cv2.fitEllipse(
                    contour1)  # if there are enough points, find the angle of the contour (using fitEllipse)
            else:
                angle1 = 0  # default
            tapeA = ReflectiveTape(wasDetected1, angle1, area1, max(leftobj1, rightobj1, key=lambda p: p.y),
                                   Point(centroid1[0], centroid1[1]),
                                   min(leftobj1, rightobj1, key=lambda p: p.y))  # jam all values into a tape object

            # SAME LOGIC AS ABOVE
            wasDetected2 = True
            contour2 = ordered[1]  # second largest tape
            leftpt2 = sorted(contour2, key=lambda a: a[0][0])[0][0]
            leftobj2 = Point(leftpt2[0], leftpt2[1])
            rightpt2 = sorted(contour2, key=lambda a: a[0][0])[-1][0]
            rightobj2 = Point(rightpt2[0], rightpt2[1])
            centroid2 = (int((leftpt2[0] + rightpt2[0]) / 2), int((leftpt2[1] + rightpt2[1]) / 2))
            area2 = cv2.contourArea(contour2)
            if len(contour2) > 5:
                (x2, y2), (MA2, ma2), angle2 = cv2.fitEllipse(contour2)
            else:
                angle2 = 0
            tapeB = ReflectiveTape(wasDetected2, angle2, area2, max(leftobj2, rightobj2, key=lambda p: p.y),Point(centroid2[0], centroid2[1]), min(leftobj2, rightobj2, key=lambda p: p.y))
            cv2.drawContours(frame, [contour1, contour2], -1, (0,255,0), 4)
            cv2.circle(frame, (centroid1[0], centroid1[1]), 7, (0,0,255), 8)
            cv2.circle(frame, (centroid2[0], centroid2[1]), 7, (255, 0, 0), 8)
            cv2.circle(frame, tuple(leftpt1), 7, (0,0, 0), 8)
            cv2.circle(frame, tuple(rightpt1), 7, (0, 0, 0), 8)
            cv2.circle(frame, tuple(leftpt2), 7, (0, 0, 0), 8)
            cv2.circle(frame, tuple(rightpt2), 7, (0, 0, 0), 8)
    else:
        tapeA = ReflectiveTape(wasDetected1, 0, 0, Point(0,0), Point(0,0), Point(0,0))
        tapeB = ReflectiveTape(wasDetected2, 0, 0, Point(0, 0), Point(0, 0), Point(0, 0))


    return frame
