from threading import Event
from threading import Lock

import cv2


class SharedFrame(object):
    def __init__(self):
        self.__frame = None
        self.__lock = Lock()
        self.__completed = Event()
        self.__frameAvaliable = Event()

    def getFrame(self):
        if self.completed: return None
        if not self.__frameAvaliable.wait(timeout=5): return None
        with self.__lock:
            currFrame = self.__frame
        self.__frameAvaliable.clear()

        return currFrame

    def setFrame(self, frame):
        with self.__lock:
            self.__frame = frame
        self.__frameAvaliable.set()

    @property
    def completed(self):
        return self.__completed.is_set()

    def notComplete(self):
        return not self.__completed.is_set()

    def markCompleted(self):
        self.__completed.set()


class Camera(object):
    def __init__(self, cameraIndex, shared_frame: SharedFrame, resolution=(200,200)):
        self.capture = cv2.VideoCapture(cameraIndex)
        self.capture.set(cv2.CAP_PROP_FRAME_WIDTH, resolution[0])
        self.capture.set(cv2.CAP_PROP_FRAME_HEIGHT, resolution[1])
        self.shared_frame = shared_frame

    def start(self):
        while self.shared_frame.notComplete():
            ret, frame = self.capture.read()
            self.shared_frame.setFrame(frame)
