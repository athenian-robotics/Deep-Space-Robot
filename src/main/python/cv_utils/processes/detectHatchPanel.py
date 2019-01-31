import cv2
import numpy
from grpc_utils.CVObject import *

#range of values to scan
low = numpy.array([0, 100, 100]) #TODO find ideal value range
high = numpy.array([10, 255, 255])


def detectHatchPanel(frame):

    blurredframe = cv2.blur(frame, (5, 5)) #blur image
    hsv = cv2.cvtColor(blurredframe, cv2.COLOR_BGR2HSV) #change colorspace to HSV
    colormask = cv2.inRange(hsv, low, high) #find tape
    contourmask, contours, hierarchy = cv2.findContours(colormask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE) #create a list of contours

    if len(contours) > 0:
        ordered = sorted(contours, key=cv2.contourArea, reverse=True) #arrange contours by area
        points = sorted(ordered[0], key=lambda a:a[0][0]) #pick largest contour and make a sorted list of points
        leftPoint = points[0][0] #choose the point with the lowest x value
        rightPoint = points[-1][0] #choose the point with the highest x value
    else:
        leftPoint = 0
        rightPoint = 0

    centroid = ((leftPoint[0]+rightPoint[0])/2, (leftPoint[1] + rightPoint[2])/2)
    diameter = rightPoint[0]-leftPoint[0]

    return GameObject(centroid, diameter)