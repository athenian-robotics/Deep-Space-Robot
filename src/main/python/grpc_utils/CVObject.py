class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y


class ReflectiveTape:
    def __init__(self, degree, size, topInside: Point, centroid: Point, bottomOutside: Point):
        self.degree = degree
        self.size = size
        self.topInside = topInside
        self.centroid = centroid
        self.bottomOutside = bottomOutside


class GaffeTape:
    def __init__(self, degree, front: Point, back: Point, centroid: Point):
        self.degree = degree
        self.front = front
        self.back = back
        self.centroid = centroid


class Hatch:
    def __init__(self, centroid: Point, diameter):
        self.centroid = centroid
        self.diameter = diameter


class Ball:
    def __init__(self, centroid: Point, diameter):
        self.centroid = centroid
        self.diameter = diameter


class ReflTapePair:
    def __init__(self, TapeA: ReflectiveTape, TapeB: ReflectiveTape):
        self.leftTape = TapeA
        self.rightTape = TapeB
