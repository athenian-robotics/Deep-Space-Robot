import numpy

from cv_utils.stream import *
from grpc_utils.CVObject import *

# hsv
low = numpy.array([77, 100, 100])
high = numpy.array([169, 255, 255])


def viewGaffeTape(frame):
    # blur image
    blurredframe = cv2.blur(frame, (2, 2))
    # change colorspace to HSV
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV)
    # find tape
    colormask = cv2.inRange(hsv, low, high)
    # create a list of contours
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE,
                                                        cv2.CHAIN_APPROX_SIMPLE)
    if len(contours) > 0:
        # arrange contours by area
        ordered = sorted(contours, key=cv2.contourArea, reverse=True)
        # biggest contour
        contour = ordered[0]
        # find the point with the smallest y value in the contour
        bottompt = sorted(contour, key=lambda a: a[0][1])[0][0]
        # find the point with the largest y value in the contour
        toppt = sorted(contour, key=lambda a: a[0][1])[-1][0]
        # find centroid as the average of ^^^
        centroid = Point(int((bottompt[0] + toppt[0]) / 2),
                         int((bottompt[1] + toppt[1]) / 2))
        if len(contour) > 4:
            # if there are enough points, find the angle of the contour (using fitEllipse)
            (x1, y1), (MA1, ma1), angle = cv2.fitEllipse(contour)
        else:
            angle = 0  # default
        print(angle)

        cv2.drawContours(frame, contour, -1, (255, 0, 0), 4)
        cv2.circle(frame, (centroid.x, centroid.y), 7, (0, 255, 0), 8)

    return cv2.bitwise_and(frame,frame, mask = contourmask)

cap = cv2.VideoCapture(1)

while 1:
    ret, frame = cap.read()
    if ret:
        ree = viewGaffeTape(frame)
        cv2.imshow(str(frame.shape), ree)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
