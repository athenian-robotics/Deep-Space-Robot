import glob

import cv2 as cv
import numpy as np


def draw(img, corners, imgpts):
    corner = tuple(corners[0].ravel())
    img = cv.line(img, corner, tuple(imgpts[0].ravel()), (255, 0, 0), 5)
    img = cv.line(img, corner, tuple(imgpts[1].ravel()), (0, 255, 0), 5)
    img = cv.line(img, corner, tuple(imgpts[2].ravel()), (0, 0, 255), 5)
    return img


# termination criteria
criteria = (cv.TERM_CRITERIA_EPS + cv.TERM_CRITERIA_MAX_ITER, 30, 0.001)

# prepare object points, like (0,0,0), (1,0,0), (2,0,0) ....,(6,5,0)
objp = np.zeros((6 * 7, 3), np.float32)
objp[:, :2] = np.mgrid[0:7, 0:6].T.reshape(-1, 2)

# Arrays to store object points and image points from all the images.
objpoints = []  # 3d point in real world space
imgpoints = []  # 2d points in image plane.

axis = np.float32([[3, 0, 0], [0, 3, 0], [0, 0, -3]]).reshape(-1, 3)

images = glob.glob('./pictures/original/*.jpg')

counter = 0

for fname in images:
    img = cv.imread(fname)
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    ret, corners = cv.findChessboardCorners(gray, (7, 6), None)
    if ret:
        objpoints.append(objp)
        corners2 = cv.cornerSubPix(gray, corners, (11, 11), (-1, -1), criteria)
        imgpoints.append(corners)

ret, mtx, dist, rvecs, tvecs = cv.calibrateCamera(objpoints, imgpoints, gray.shape[::-1], None, None)
np.savez("calibration_matrix", ret=ret, mtx=mtx, dist=dist, rvecs=rvecs, tvecs=tvecs)

for fname in images:
    orig_img = cv.imread(fname)
    gray = cv.cvtColor(orig_img, cv.COLOR_BGR2GRAY)

    h, w, channel = orig_img.shape
    newcameramtx, roi = cv.getOptimalNewCameraMatrix(mtx, dist, (w, h), 1, (w, h))

    dst = cv.undistort(orig_img, mtx, dist, None, newcameramtx)
    x, y, w, h = roi
    dst = dst[y:y + h, x:x + w]


    while True:
        cv.imshow("orig", orig_img)
        cv.imshow("res", dst)
        if cv.waitKey(1) & 0xFF == ord('q'):
            break

cv.destroyAllWindows()

"""
for fname in glob.glob('left*.jpg'):
    img = cv2.imread(fname)
    gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    ret, corners = cv2.findChessboardCorners(gray, (7,6),None)

    if ret == True:
        corners2 = cv2.cornerSubPix(gray,corners,(11,11),(-1,-1),criteria)

        # Find the rotation and translation vectors.
        rvecs, tvecs, inliers = cv2.solvePnPRansac(objp, corners2, mtx, dist)

        # project 3D points to image plane
        imgpts, jac = cv2.projectPoints(axis, rvecs, tvecs, mtx, dist)

        img = draw(img,corners2,imgpts)
        cv2.imshow('img',img)
        k = cv2.waitKey(0) & 0xff
        if k == 's':
            cv2.imwrite(fname[:6]+'.png', img)

cv2.destroyAllWindows()
"""
