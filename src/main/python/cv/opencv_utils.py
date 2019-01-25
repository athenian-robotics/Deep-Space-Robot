import datetime
import logging
import platform

import cv2

RED = (0, 0, 255)
GREEN = (0, 255, 0)
BLUE = (255, 0, 0)
YELLOW = (0, 255, 255)


def get_center(contour):
    momt = cv2.moments(contour)
    area = int(momt["m00"])
    return int(momt["m10"] / area), int(momt["m01"] / area)

def save_image(frame):
    file_name = "ct-{0}.png".format(datetime.datetime.now().strftime("%H-%M-%S"))
    cv2.imwrite(file_name, frame)
    logging.info("Wrote image to {0}".format(file_name))


def is_raspi():
    return platform.system() == "Linux"
