import cv2
import imutils
import numpy as np

from arc852 import camera
from opencv_utils import BLUE, GREEN, RED, is_raspi, get_center


def main():
    cam = camera.Camera()

    #    Orange
    bgr = [39, 75, 217]

    hsv_range = 20

    bgr_img = np.uint8([[bgr]])
    hsv_img = cv2.cvtColor(bgr_img, cv2.COLOR_BGR2HSV)
    hsv_value = hsv_img[0, 0, 0]

    lower = np.array([hsv_value - hsv_range, 100, 100])
    upper = np.array([hsv_value + hsv_range, 255, 255])

    print(lower, upper)

    # lower = np.array([0,136,0])
    # upper = np.array([80, 255, 255])

    try:
        cnt = 0
        while cam.is_open():

            # Read and resize image
            image = cam.read()
            image = imutils.resize(image, width=600)

            # Convert image to HSV
            hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

            # Create mask
            in_range_mask = cv2.inRange(hsv_image, lower, upper)

            # Bitwise-AND mask and original image
            in_range_result = cv2.bitwise_and(image, image, mask=in_range_mask)

            # Convert to grayscale
            gs_image = cv2.cvtColor(in_range_result, cv2.COLOR_BGR2GRAY)

            # Get all contours
            contours, hierarchy = cv2.findContours(gs_image, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

            # Remove small contours
            eligible = [c for c in contours if cv2.contourArea(c) >= 10]

            # Sort images
            ordered = sorted(eligible, key=lambda c: cv2.contourArea(c), reverse=True)

            text = 'Frame #: {}'.format(cnt)

            # Grab largest contour
            if len(ordered) > 0:
                largest = ordered[0]

                # Draw a bounding box around contour
                cv2.drawContours(image, [largest], -1, GREEN, 2)

                # Draw center of contour
                center_x, center_y = get_center(largest)
                cv2.circle(image, (center_x, center_y), 4, RED, -1)

                # Add centroid to image text
                text = "{} ({}, {})".format(text, center_x, center_y)
            else:
                text = "{} (no match)".format(text)

            cv2.putText(img=image,
                        text=text,
                        org=(10, 25),
                        fontFace=cv2.FONT_HERSHEY_SIMPLEX,
                        fontScale=(.55 if is_raspi() else .75),
                        color=RED,
                        thickness=2)

            # Display images
            cv2.imshow('Original', image)

            key = cv2.waitKey(30) & 0xFF

            if key == ord('q'):
                break

            cnt += 1
    finally:
        cv2.destroyAllWindows()
        if cam.is_open():
            cam.close()


if __name__ == "__main__":
    main()
