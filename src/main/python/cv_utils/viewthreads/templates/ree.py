import math

import numpy as np

from cv_utils.stream import *


# Tape Pair Class for finding tape pairs
# https://www.learnopencv.com/head-pose-estimation-using-opencv-and-dlib/
# https://opencv-python-tutroals.readthedocs.io/en/latest/py_tutorials/py_calib3d/py_pose/py_pose.html
# https://opencv-python-tutroals.readthedocs.io/en/latest/py_tutorials/py_calib3d/py_calibration/py_calibration.html

class TapePairs:
    def __init__(self, contours, center):
        self.center = center
        self.contours = contours
        self.contourPairs = []
        self.pair()

    # pair up all possible pairs
    def pair(self):
        self.contourPairs.clear()
        for i in range(len(self.contours) - 1):
            self.contourPairs.append([self.contours[i], self.contours[i + 1]])
        self.contourPairs = list(filter(lambda contourPair: self.getIntersection(contourPair), self.contourPairs))

    # estimate the angles and see if the pairs are valid
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

    # find centroid of contour
    def findCentroid(self, contour):
        leftpt = sorted(contour, key=lambda a: a[0][0])[0][0]
        rightpt = sorted(contour, key=lambda a: a[0][0])[-1][0]
        centroid = (int((leftpt[0] + rightpt[0]) / 2), int((leftpt[1] + rightpt[1]) / 2))
        return centroid

    # get distance from center
    def closeCenter(self, contourCenter0, contourCenter1, center):
        return getDistance(contourCenter0, center) + getDistance(contourCenter1, center)

    # return the closest tape pair to the center
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


# Values that needs to be tuned at the practice field
deadZone = 50
stopDistance = 350
blobMax = 30000
blobMin = 200

# hsv range
hrange = 27
srange = 83
vrange = 38

# hsv base
bhue = 68
bsat = 174
bval = 62

low = np.array([bhue - hrange, bsat - srange, bval - vrange])
high = np.array([bhue + hrange, bsat + srange, bval + vrange])

# 3D object points

# left tape
# top left: -6.936, -2.9127, 0
# top right: -5, -2.4127, 0
# bottom left: -8.311, 2.4127, 0
# bottom right: -6.375, 2.9127, 0

# right tape
# top left: 5, -2.4127
# top right: 6.936, -2.9127
# bottom left: 6.375, 2.9127
# bottom right: 8.311, 2.4127

object_points = np.array([
    # left tape
    (-6.936, -2.9127, 0),  # top left
    (-5, -2.4127, 0),  # top right
    (-8.311, 2.4127, 0),  # bottom left
    (-6.375, 2.9127, 0),  # bottom right

    # right tape
    (5, -2.4127, 0),  # top left
    (6.936, -2.9127, 0),  # top right
    (6.275, 2.9127, 0),  # bottom left
    (8.311, 2.4127, 0)  # bottom right
])

# Load previously saved data for camera calibration, undistortion
with np.load('./src/main/python/cv_utils/calibration_matrix.npz') as X:
    mtx, dist, _, _ = [X[i] for i in ('mtx', 'dist', 'rvecs', 'tvecs')]


# undistort camera based on saved data and info
def undistort(img, w, h):
    newcameramtx, roi = cv2.getOptimalNewCameraMatrix(mtx, dist, (w, h), 1, (w, h))
    dst = cv2.undistort(img, mtx, dist, None, newcameramtx)
    x, y, w, h = roi
    dst = dst[y:y + h, x:x + w]
    return dst


# distance formula
def getDistance(p1, p2):
    return int(math.sqrt((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2))


# draw cube for visual effect on image
def drawCube(img, rvecs, tvecs):
    axis = np.float32([[0, 0, 0], [0, 3, 0], [3, 3, 0], [3, 0, 0], [0, 0, -3], [0, 3, -3], [3, 3, -3], [3, 0, -3]])
    imgpts, jac = cv2.projectPoints(axis, rvecs, tvecs, mtx, dist)
    imgpts = np.int32(imgpts).reshape(-1, 2)

    # draw ground floor in green
    img = cv2.drawContours(img, [imgpts[:4]], -1, (0, 255, 0), -3)
    # draw pillars in blue color
    for i, j in zip(range(4), range(4, 8)): img = cv2.line(img, tuple(imgpts[i]), tuple(imgpts[j]), (255), 3)
    # draw top layer in red color
    img = cv2.drawContours(img, [imgpts[4:]], -1, (0, 0, 255), 3)

    return img


# get minimum bounding rectangle
def minBoundRect(img, contour):
    rect = cv2.minAreaRect(contour)
    box = cv2.boxPoints(rect)
    cv2.drawContours(img, [np.int0(box)], 0, (0, 255, 255), 2)
    return box


# get minimum bounding quadrilateral
def minBoundQuad(img, contour):
    epsilon = 0.03 * cv2.arcLength(contour, True)
    approx = cv2.approxPolyDP(contour, epsilon, True)
    cv2.drawContours(img, [approx], 0, (255, 255, 255), 3)
    return approx


# sort the quadrilateral points so the points are in the correct order with the Object Points
def sortBox(box):
    pb = tuple(tuple(row) for row in box)
    if len(pb) != 4: raise TypeError
    c = ((pb[0][0][0] + pb[1][0][0] + pb[2][0][0] + pb[3][0][0]) / 4,
         (pb[0][0][1] + pb[1][0][1] + pb[2][0][1] + pb[3][0][1]) / 4)
    top = tuple(filter(lambda p: p[0][1] < c[1], pb))
    bot = tuple(filter(lambda p: p[0][1] >= c[1], pb))

    if len(top) != 2 or len(bot) != 2: raise TypeError
    if top[0][0][0] > top[1][0][0]: top = top[::-1]
    if bot[0][0][0] > bot[1][0][0]: bot = bot[::-1]

    bp = top + bot
    bp = [np.array(np.array(tuple(map(float, pp[0])))) for pp in bp]
    return bp


# main method that runs everything
def viewReflTape(shared_frame: SharedFrame):
    frame = shared_frame.getFrame()

    height, width, channels = frame.shape
    frame = undistort(frame, width, height)

    center = (int(width / 2), int(height / 2))
    cv2.circle(frame, (center), 7, (255, 255, 255), 2)

    blurredframe = cv2.blur(frame, (5, 5))
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)
    colormask = cv2.inRange(hsv, low, high)
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    filteredContours = list(filter(lambda contour: blobMin <= cv2.contourArea(contour) <= blobMax, contours))
    ordered = sorted(filteredContours, key=lambda contour: contour[0][0][0])

    if len(ordered) >= 2:
        try:
            currPair = TapePairs(ordered, center)
            c0, c1 = currPair.getPair()

            contour0 = c0[0]
            centroid0 = c0[1]
            box0 = minBoundQuad(frame, contour0)

            contour1 = c1[0]
            centroid1 = c1[1]
            box1 = minBoundQuad(frame, contour1)

            pbox0 = sortBox(box0)
            pbox1 = sortBox(box1)

            try:
                image_points = np.array(pbox0 + pbox1)
            except TypeError:
                raise TypeError

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
            success, rotation_vector, translation_vector = cv2.solvePnP(object_points, image_points, mtx, dist)

            # print("Rotational Vector: \n{}".format(rotation_vector))
            # print("Translation Vector: \n{}".format(translation_vector))

            cv2.circle(frame, (pairCentroid[0], pairCentroid[1]), 7, (255, 0, 0), -1)
            return drawCube(frame, rotation_vector, translation_vector)

        # return unmodified frame
        except TypeError as e:
            print(e)
            return frame

        except AssertionError as e:
            print(e)
            return frame

    return frame
