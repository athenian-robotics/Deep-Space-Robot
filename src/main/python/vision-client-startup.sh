#!/usr/bin/env bash

# Tune exposure on camera
v4l2-ctl -d /dev/video0 -c exposure_auto=1 -c exposure_absolute=20

python3 visionClient.py