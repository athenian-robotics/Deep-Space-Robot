import cv2

webcam = cv2.VideoCapture(0)


def nothing(x):
    pass


# Creating a window for later use
cv2.namedWindow('hsvTrackbar')

# cv.CreateTrackbar(trackbarName, windowName, value, count, onChange)  None
cv2.createTrackbar('Hue', 'hsvTrackbar', 0, 180, nothing)  # default 0 205 255 69 8 12
cv2.createTrackbar('Sat', 'hsvTrackbar', 205, 255, nothing)
cv2.createTrackbar('Val', 'hsvTrackbar', 255, 255, nothing)
cv2.createTrackbar('Hrange', 'hsvTrackbar', 69, 127, nothing)
cv2.createTrackbar('Srange', 'hsvTrackbar', 69, 127, nothing)
cv2.createTrackbar('Vrange', 'hsvTrackbar', 69, 127, nothing)

while True:
    ret, frame = webcam.read()
    frame = cv2.GaussianBlur(frame, (5, 5), 0)
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    # get info from track bar and apply to result
    hue = cv2.getTrackbarPos('Hue', 'hsvTrackbar')
    sat = cv2.getTrackbarPos('Sat', 'hsvTrackbar')
    val = cv2.getTrackbarPos('Val', 'hsvTrackbar')
    hrange = cv2.getTrackbarPos('Hrange', 'hsvTrackbar')
    srange = cv2.getTrackbarPos('Srange', 'hsvTrackbar')
    vrange = cv2.getTrackbarPos('Vrange', 'hsvTrackbar')

    colorLower = (hue - hrange, sat - srange, val - vrange)
    colorUpper = (hue + hrange, sat + srange, val + vrange)

    filteredFrame = cv2.inRange(hsv, colorLower, colorUpper)
    colorCutout = cv2.bitwise_and(frame, frame, mask=filteredFrame)

    cv2.imshow('hsvTrackbar', colorCutout)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        print("hue: {0}\n"
              "hue range: {1}\n\n"
              "sat: {2}\n"
              "saturation range: {3}\n\n"
              "val: {4}\n"
              "value range: {5}\n\n".format(hue, hrange, sat, srange, val, vrange))
        break

webcam.release()
cv2.destroyAllWindows()
