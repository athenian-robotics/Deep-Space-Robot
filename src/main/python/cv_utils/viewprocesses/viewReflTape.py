import math

import numpy

from cv_utils.stream import *

# range of values to scan
# low = numpy.array([50, 100, 50])
# high = numpy.array([70, 200, 200])

# # bgr blue
# low = numpy.array([85, 0, 0])
# high = numpy.array([255, 150, 150])

# hsv
hrange = 47
srange = 55
vrange = 47

low = numpy.array([85 - hrange, 235 - srange, 80 - vrange])
high = numpy.array([85 + hrange, 235 + srange, 80 + vrange])

# low = numpy.array([45, 40, 0])
# high = numpy.array([155, 200, 160])

# Constants that needs to be tuned
deadZone = 50
stopDistance = 350
blobMax = 30000
blobMin = 200


class TapePairs:
    def __init__(self, contours, center):
        self.center = center
        self.contours = contours
        self.contourPairs = []
        self.pair()

    def pair(self):
        self.contourPairs.clear()
        for i in range(len(self.contours) - 1):
            self.contourPairs.append([self.contours[i], self.contours[i + 1]])
        self.contourPairs = list(filter(lambda contourPair: self.getIntersection(contourPair), self.contourPairs))

    # return if the intersection is above the centroid or not
    def getIntersection(self, contourPair):
        def lfrPT(contour):
            leftpt = sorted(contour, key=lambda a: a[0][0])[0][0]
            rightpt = sorted(contour, key=lambda a: a[0][0])[-1][0]
            return leftpt, rightpt

        def estimateAngle(lefpt, rightpt):
            # right is: /  \ : left
            if lefpt[1] < rightpt[1]:
                return "l"
            elif lefpt[1] > rightpt[1]:
                return "r"
            else:
                return None

        leftpt0, rightpt0 = lfrPT(contourPair[0])
        leftpt1, rightpt1 = lfrPT(contourPair[1])

        angle0 = estimateAngle(leftpt0, rightpt0)
        angle1 = estimateAngle(leftpt1, rightpt1)

        return angle0 == "r" and angle1 == "l"

    def findCentroid(self, contour):
        leftpt = sorted(contour, key=lambda a: a[0][0])[0][0]
        rightpt = sorted(contour, key=lambda a: a[0][0])[-1][0]
        centroid = (int((leftpt[0] + rightpt[0]) / 2), int((leftpt[1] + rightpt[1]) / 2))
        return centroid

    def getPair(self):
        try:
            list.sort(self.contourPairs, key=lambda contourPair: self.closeCenter(self.findCentroid(contourPair[0]),
                                                                                  self.findCentroid(contourPair[1]),
                                                                                  self.center))
            c0 = self.contourPairs[0][0]
            c1 = self.contourPairs[0][1]
            return [c0, self.findCentroid(c0)], [c1, self.findCentroid(c1)]
        except IndexError:
            pass

    def closeCenter(self, contourCenter0, contourCenter1, center):
        return getDistance(contourCenter0, center) + getDistance(contourCenter1, center)


def getMinMax(min, max, val):
    return min <= val <= max


def getDistance(p1, p2):
    return int(math.sqrt((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2))


def viewReflTape(frame):
    # frame = shared_frame.getFrame()
    height, width, channels = frame.shape
    center = (int(width / 2), int(height / 2))
    cv2.circle(frame, (center), 7, (255, 255, 255), 5)

    blurredframe = cv2.blur(frame, (5, 5))
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)
    colormask = cv2.inRange(hsv, low, high)
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    filteredContours = list(filter(lambda contour: getMinMax(blobMin, blobMax, cv2.contourArea(contour)), contours))
    ordered = sorted(filteredContours, key=lambda contour: contour[0][0][0])

    if len(ordered) >= 2:
        try:
            currPair = TapePairs(ordered, center)
            c0, c1 = currPair.getPair()

            contour0 = c0[0]
            centroid0 = c0[1]

            contour1 = c1[0]
            centroid1 = c1[1]
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
    if ret: cv2.imshow(str(frame.shape), viewReflTape(frame))
    if cv2.waitKey(1) & 0xFF == ord('q'): break

cap.release()
cv2.destroyAllWindows()
