from threading import Thread

import cv2


class Webcam:

    def __init__(self):
        self.video_capture = cv2.VideoCapture(1)
        self.current_frame = self.video_capture.read()[1]

    # create thread for capturing images
    def start(self):
        Thread(target=self._update_frame, args=()).start()

    def _update_frame(self):
        while (True):
            self.current_frame = self.video_capture.read()[1]

    # get the current frame
    def get_current_frame(self):
        return self.current_frame


webcam = Webcam()
webcam.start()
counter = 3

while True:
    frame = image = webcam.get_current_frame()
    # display image 4.png
    cv2.imshow('grid', frame)

    # save image to file, if pattern found2
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    ret, corners = cv2.findChessboardCorners(gray, (7, 6), None)

    if ret:
        print("Pattern Detected")
        filename = '{0}.jpg'.format(counter)
        cv2.imwrite(filename, frame)
        counter += 1

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cv2.destroyAllWindows()
