import math

import numpy

from cv_utils.stream import *

# range of values to scan
# low = numpy.array([50, 100, 50])
# high = numpy.array([70, 200, 200])

# hsv
low = numpy.array([77, 100, 100])
high = numpy.array([169, 255, 200])

# bgr
#low = numpy.array([70, 0, 0])
#high = numpy.array([255, 200, 200])

# Constants
deadZone = 50
stopDistance = 350
blobMax = 35000
blobMin = 500


def findCentroid(contour):
    leftpt = sorted(contour, key=lambda a: a[0][0])[0][0]
    rightpt = sorted(contour, key=lambda a: a[0][0])[-1][0]
    centroid = (int((leftpt[0] + rightpt[0]) / 2), int((leftpt[1] + rightpt[1]) / 2))
    return centroid


# TODO FIND OUT WHAT
def findNearestContour(contours, center):
    d = {}
    for contour in contours:
        if (cv2.contourArea(contour) <= blobMin): pass
        distance = getDistance(findCentroid(contour), center)
        d[distance] = contour
    dict(sorted(d, key=d.items(), reverse=True))


def estimateAngle(degree) -> str:
    if 15 <= degree <= 90:
        return "l"
    elif 100 < degree <= 180:
        return "r"
    else:
        return None


def findProperties(contour):
    size = cv2.contourArea(contour)
    if getMinMax(blobMin, blobMax, size):
        centroid = findCentroid(contour)
        if len(contour) >= 5:
            (x1, y1), (MA1, ma1), angle = cv2.fitEllipse(contour)
        else:
            angle = 0

        direction = estimateAngle(angle)
        return centroid, direction, size

    return None


def getMinMax(min, max, val):
    return (val >= min and val <= max)


def getDistance(p1, p2):
    return int(math.sqrt((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2))


def viewReflTape(frame):
    height, width, channels = frame.shape
    center = (int(width / 2), int(height / 2))

    blurredframe = cv2.blur(frame, (5, 5))
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)
    colormask = cv2.inRange(hsv, low, high)
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    if len(contours) >= 2:
        ordered = sorted(contours, key=cv2.contourArea, reverse=True)

        contour0 = ordered[0]  # biggest contour
        contour1 = ordered[1]  # second largest tape

        try:
            centroid0, direction0, size0 = findProperties(contour0)
            centroid1, direction1, size1 = findProperties(contour1)

            # assert pairs
            assert ((direction0 == 'l' and direction1 == 'r') or (direction1 == 'l' and direction0 == 'r'))
            cv2.drawContours(frame, [contour0, contour1], -1, (0, 255, 0), 4)

            pairCentroid = (int((centroid0[0] + centroid1[0]) / 2), int((centroid0[1] + centroid1[1]) / 2))
            distance = getDistance(centroid0, centroid1)

            indicatorLength = int((stopDistance - distance) * 1.2) if (stopDistance - distance >= 0) else 0
            indicatorColor = (0, 255, 0)

            # visual indicator
            if indicatorLength <= 30:
                indicatorColor = (0, 0, 255)
                indicatorLength = 1000

            # visual feedback

            cv2.line(frame, (center[0] - indicatorLength, height - 50), (center[0] + indicatorLength, height - 50),
                     indicatorColor, 25)
            cv2.circle(frame, (pairCentroid[0], pairCentroid[1]), 7, (255, 0, 0), 8)
            return frame

        except TypeError:
            pass

        except AssertionError:
            pass

    return frame


cap = cv2.VideoCapture(1)

while 1:
    ret, frame = cap.read()
    if ret:
        ree = viewReflTape(frame)
        cv2.imshow(str(frame.shape), ree)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
