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


class CVData:
    def __init__(self, leftTape: ReflectiveTape, rightTape: ReflectiveTape, gaffeTape: GaffeTape):
        self.leftTape = leftTape
        self.rightTape = rightTape
        self.gaffeTape = gaffeTape
