import csv
import sys
import math
import time
import turtle

def draw(fname=None):
    try:
        # Default to path_test.csv if no file given
        fname = fname or 'path_test.csv'

        # Read csv file
        with open(fname) as f:
            l = list(csv.reader(f))

        # Cast values from strings to floats
        l = [map(float, p) for p in l]

        # Calculate delay between frames so that the animation lasts 5 seconds
        delay = 5.0 / len(l)

        # Create turtle
        t = turtle.Turtle()
        t.speed(0)

        # Draw path
        for p in l:
            # Set rotation and position (arbitrary scaling factor of 40)
            t.setheading(math.degrees(math.atan2(p[3], p[2])))
            t.goto(p[0] * 40, p[1] * 40)

            # Wait a bitdelay between frames
            time.sleep(delay)

        # Pause so user can see the completed result
        win = t.getscreen()
        win.exitonclick()
    except KeyboardInterrupt:
        pass

if __name__ == '__main__':
    # Use .csv file passed in as command-line argument, if present
    fname = (sys.argv + [None])[1]
    draw(fname)
