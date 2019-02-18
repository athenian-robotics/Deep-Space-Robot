class Vector3D:
    def __init__(self, x: object, y: object, z: object) -> object:
        self.x = x
        self.y = y
        self.z = z


class CameraPose:
    def __init__(self, translation: Vector3D, rotational: Vector3D):
        self.translation = translation
        self.rotational = rotational

# class Point:
#     def __init__(self, x, y):
#         self.x = x
#         self.y = y
#
#
# class GaffeTape:
#     def __init__(self, degree, front: Point, back: Point, centroid: Point):
#         self.degree = degree
#         self.front = front
#         self.back = back
#         self.centroid = centroid
#
#
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
#
# class ReflectiveTape:
#     def __init__(self, blobsize):
#         self.blobsize = blobsize
#
#
# class ReflTapePair:
#     def __init__(self, TapeA: ReflectiveTape, TapeB: ReflectiveTape, Centroid: Point, Distance):
#         self.leftTape = TapeA
#         self.rightTape = TapeB
#         self.centroid = Centroid
#         self.distance = Distance
#
