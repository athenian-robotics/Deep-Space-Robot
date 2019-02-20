#!/usr/bin/env bash

# TODO find whether settings persist through reboot
# TODO find out why the fuck it's being so inconsistent

# Tune exposure on camera
#v4l2-ctl -d /dev/video1 -c exposure_auto=1 -c exposure_absolute=3 -c white_balance_temperature_auto=0 -c white_balance_temperature=5000
v4l2-ctl -d /dev/video1 -c exposure_auto=1 -c exposure_absolute=3 -c white_balance_temperature_auto=0 -c white_balance_temperature=5000
v4l2-ctl -d /dev/video0 -c exposure_auto=1 -c exposure_absolute=9 -c brightness=150

# v4l2-ctl -d /dev/video2 -c exposure_auto=1
# v4l2-ctl -d /dev/video3 -c exposure_auto=1

# python3 ./cv_utils/samples/hsvTuner.py
# python3 overhead.py
# python3 -c 'import cv_utils.viewthreads.viewReflTape.py'
python3 ./src/main/python/visionClient.py
#python3 ./src/main/python/cv_utils/viewthreads/overhead.py
