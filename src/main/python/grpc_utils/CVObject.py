class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y


class ReflectiveTape:
    def __init__(self, blobsize):
        self.blobsize = blobsize


class ReflTapePair:
    def __init__(self, TapeA: ReflectiveTape, TapeB: ReflectiveTape, Centroid: Point, Distance):
        self.leftTape = TapeA
        self.rightTape = TapeB
        self.centroid = Centroid
        self.distance = Distance


class GaffeTape:
    def __init__(self, degree, front: Point, back: Point, centroid: Point):
        self.degree = degree
        self.front = front
        self.back = back
        self.centroid = centroid

# class Hatch:
#     def __init__(self, centroid: Point, diameter):
#         self.centroid = centroid
#         self.diameter = diameter
#
#
# class Ball:
#     def __init__(self, centroid: Point, diameter):
#         self.centroid = centroid
#         self.diameter = diameter
