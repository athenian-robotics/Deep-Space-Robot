import math

import numpy

from cv_utils.stream import *

# range of values to scan
low = numpy.array([50, 100, 50])
high = numpy.array([70, 200, 200])

# hsv blue tape
# low = numpy.array([77, 0, 0])
# high = numpy.array([169, 255, 255])

# bgr blue tape
# low = numpy.array([60, 0, 0])
# high = numpy.array([255, 255, 255])

deadZone = 30


def viewReflTape(shared_frame: SharedFrame):
    frame = shared_frame.getFrame()
    height, width, channels = frame.shape
    wCenter = int(width / 2)

    totalArea = 0
    distance = 0

    cv2.line(frame, (wCenter - deadZone, 0), (wCenter - deadZone, height), (0, 0, 255), 2)
    cv2.line(frame, (wCenter + deadZone, 0), (wCenter + deadZone, height), (0, 0, 255), 2)

    blurredframe = cv2.blur(frame, (5, 5))  # blur image
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)  # change colorspace to HSV
    colormask = cv2.inRange(hsv, low, high)  # find tape
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE,
                                                        cv2.CHAIN_APPROX_SIMPLE)  # create a list of contours

    if len(contours) > 0:
        ordered = sorted(contours, key=cv2.contourArea, reverse=True)  # arrange contours by area
        if len(ordered) > 1:
            contour1 = ordered[0]  # biggest contour
            area1 = cv2.contourArea(contour1)  # find area of contour

            contour2 = ordered[1]  # second largest tape
            area2 = cv2.contourArea(contour2)

            totalArea = area1 + area2

            if (totalArea <= 500):
                return frame, 0

            leftpt1 = sorted(contour1, key=lambda a: a[0][0])[0][0]
            rightpt1 = sorted(contour1, key=lambda a: a[0][0])[-1][0]
            centroid1 = (int((leftpt1[0] + rightpt1[0]) / 2), int((leftpt1[1] + rightpt1[1]) / 2))

            leftpt2 = sorted(contour2, key=lambda a: a[0][0])[0][0]
            rightpt2 = sorted(contour2, key=lambda a: a[0][0])[-1][0]
            centroid2 = (int((leftpt2[0] + rightpt2[0]) / 2), int((leftpt2[1] + rightpt2[1]) / 2))

            cv2.drawContours(frame, [contour1, contour2], -1, (0, 255, 0), 4)

            distance = int(math.sqrt((centroid1[0] - centroid2[0]) ** 2 + (centroid1[1] - centroid2[1]) ** 2))
            pairCentroid = (int((centroid1[0] + centroid2[0]) / 2), int((centroid1[1] + centroid1[1]) / 2))

            if (deadZone <= abs(width / 2 - pairCentroid[0])):
                cv2.line(frame, (wCenter - deadZone, 0), (wCenter - deadZone, height), (0, 255, 0), 2)
                cv2.line(frame, (wCenter + deadZone, 0), (wCenter + deadZone, height), (0, 255, 0), 2)

            cv2.circle(frame, (pairCentroid[0], pairCentroid[1]), 7, (255, 0, 0), 8)
            cv2.circle(frame, (centroid1[0], centroid1[1]), 7, (0, 0, 255), 5)
            cv2.circle(frame, (centroid2[0], centroid2[1]), 7, (0, 0, 255), 5)

    return frame
