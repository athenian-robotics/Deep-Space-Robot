class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y


class ReflectiveTape:
    def __init__(self, wasDetected, degree, size, topInside: Point, centroid: Point, bottomOutside: Point):
        self.wasDetected = wasDetected
        self.degree = degree
        self.size = size
        self.topInside = topInside
        self.centroid = centroid
        self.bottomOutside = bottomOutside


class GaffeTape:
    def __init__(self, wasDetected, degree, front: Point, back: Point, centroid: Point):
        self.wasDetected = wasDetected
        self.degree = degree
        self.front = front
        self.back = back
        self.centroid = centroid


class Hatch:
    def __init__(self, wasDetected, centroid: Point, diameter):
        self.wasDetected = wasDetected
        self.centroid = centroid
        self.diameter = diameter


class Ball:
    def __init__(self, wasDetected, centroid: Point, diameter):
        self.wasDetected = wasDetected
        self.centroid = centroid
        self.diameter = diameter


class CVData:
    def __init__(self, leftTape: ReflectiveTape, rightTape: ReflectiveTape, gaffeTape: GaffeTape):
        self.leftTape = leftTape
        self.rightTape = rightTape
        self.gaffeTape = gaffeTape


class DoubleTape:
    def __init__(self, TapeA: ReflectiveTape, TapeB: ReflectiveTape):
        self.tapeA = TapeA
        self.tapeB = TapeB
