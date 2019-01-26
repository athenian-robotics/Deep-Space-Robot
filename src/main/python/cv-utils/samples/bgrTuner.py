import cv2
import numpy as np

webcam = cv2.VideoCapture(0)


def nothing(x):
    pass


# Creating a window for later use
cv2.namedWindow('bgrTrackbar')

# cv.CreateTrackbar(trackbarName, windowName, value, count, onChange)  None
cv2.createTrackbar('Low Red', 'bgrTrackbar', 0, 255, nothing)  # default 0 205 255 69 8 12
cv2.createTrackbar('Low Green', 'bgrTrackbar', 0, 255, nothing)
cv2.createTrackbar('Low Blue', 'bgrTrackbar', 0, 255, nothing)
cv2.createTrackbar('High Red', 'bgrTrackbar', 0, 255, nothing)
cv2.createTrackbar('High Green', 'bgrTrackbar', 0, 255, nothing)
cv2.createTrackbar('High Blue', 'bgrTrackbar', 0, 255, nothing)

while True:
    ret, frame = webcam.read()
    frame = cv2.GaussianBlur(frame, (5, 5), 0)
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    # get info from track bar and apply to result
    hr = cv2.getTrackbarPos('High Red', 'bgrTrackbar')
    lr = cv2.getTrackbarPos('Low Red', 'bgrTrackbar')

    hb = cv2.getTrackbarPos('High Blue', 'bgrTrackbar')
    lb = cv2.getTrackbarPos('Low Blue', 'bgrTrackbar')

    hg = cv2.getTrackbarPos('High Green', 'bgrTrackbar')
    lg = cv2.getTrackbarPos('Low Green', 'bgrTrackbar')

    low = np.array([lb, lg, lr])
    high = np.array([hb, hg, hr])

    mask = cv2.inRange(hsv, low, high)
    colorCutout = cv2.bitwise_and(frame, frame, mask=mask)

    cv2.imshow('bgrTrackbar', colorCutout)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        print("High red: {0}\n"
              "low red: {1}\n\n"
              "high blue: {2}\n"
              "low blue: {3}\n\n"
              "high green: {4}\n"
              "low green: {5}\n\n".format(hr, lr, hb, lb, hg, lg))
        break


webcam.release()
cv2.destroyAllWindows()