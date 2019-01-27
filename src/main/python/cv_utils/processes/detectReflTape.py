import cv2
import numpy

from grpc_utils.CVObject import *
#Range of values to scan
low = numpy.array([100, 100, 50])
high = numpy.array([110, 255, 255])


def detectReflTape(frame):

    blurredframe = cv2.blur(frame, (5, 5)) #blur image
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV) #change colorspace to HSV
    colormask = cv2.inRange(hsv, low, high) #find tape
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE) #create a list of contours
    viewmask = cv2.bitwise_and(blurredframe, blurredframe, mask=contourmask) #combine the original frame with the mask for testing


    if len(contours) > 0:
        ordered = sorted(contours, key=cv2.contourArea, reverse=True)
        if len(ordered) > 1:

            tape1 = ordered[0]
            left1 = sorted(tape1, key=lambda a:a[0][0])[0][0]
            left12 = Point(left1[0], left1[1])
            right1 = sorted(tape1, key=lambda a:a[0][0])[-1][0]
            right12 = Point(right1[0], right1[1])

            centroid1 = (int((left1[0] + right1[0]) / 2), int((left1[1] + right1[1]) / 2))
            cv2.circle(viewmask, tuple(left1), 25, (255, 0, 255), 5)
            cv2.circle(viewmask, tuple(right1), 40, (255, 0, 255), 5)
            cv2.circle(viewmask, tuple(centroid1), 10, (255, 255, 255))
            area1 = cv2.contourArea(tape1)
            if len(tape1) > 5:
                (x1, y1), (MA1, ma1), angle1 = cv2.fitEllipse(tape1)
            else:
                angle1 = -1
            tapeA = ReflectiveTape(angle1, area1, max(left12, right12, key = lambda p: p.y), Point(centroid1[0], centroid1[1]), min(left12, right12, key = lambda p: p.y))



            tape2 = ordered[0]
            left2 = sorted(tape1, key=lambda a:a[0][0])[0][0]
            left22 = Point(left2[0], left2[1])
            right2 = sorted(tape2, key=lambda a:a[0][0])[-1][0]
            right22 = Point(right2[0], right2[1])

            centroid2 = (int((left2[0] + right2[0]) / 2), int((left2[1] + right2[1]) / 2))
            cv2.circle(viewmask, tuple(left2), 25, (255, 0, 255), 5)
            cv2.circle(viewmask, tuple(right2), 40, (255, 0, 255), 5)
            cv2.circle(viewmask, tuple(centroid2), 10, (255, 255, 255))
            area2 = cv2.contourArea(tape2)
            if len(tape2) > 5:
                (x2, y2), (MA2, ma2), angle2 = cv2.fitEllipse(tape2)
            else:
                angle2 = -1
            tapeB = ReflectiveTape(angle2, area2, max(left22, right22, key = lambda p: p.y), Point(centroid2[0], centroid2[1]), min(left22, right22, key = lambda p: p.y))

            cv2.imshow("k", viewmask)

            return DoubleTape(tapeA, tapeB)


while True:
    ret, cashmoney = cv2.VideoCapture(1).read()
    detectReflTape(cashmoney)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break