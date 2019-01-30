#!/usr/bin/env python
# -*- coding: utf-8 -*-

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


class Camera:
    def __init__(self, cameraIndex, shared_frame: SharedFrame):
        self.capture = cv2.VideoCapture(cameraIndex)
        self.shared_frame = shared_frame

        # Thread(target=self.sendFrame, args=(shared_frame,)).start()

    def start(self):
        while True:
            ret, frame = self.capture.read()
            self.shared_frame.setFrame(frame)
