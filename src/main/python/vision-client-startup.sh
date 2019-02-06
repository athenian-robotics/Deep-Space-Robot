#!/usr/bin/env bash

# Tune exposure on camera
v4l2-ctl -d /dev/video1 -c exposure_auto=1 -c exposure_absolute=3 -c white_balance_temperature_auto=0 -c white_balance_temperature=5000

# python3 ./cv_utils/samples/bgrTuner.py
python3 test.py