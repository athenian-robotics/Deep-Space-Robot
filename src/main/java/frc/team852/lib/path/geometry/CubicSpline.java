package frc.team852.lib.geometry;

import frc.team852.lib.utilities.Pose2D;
import frc.team852.lib.utilities.Rotation2D;
import frc.team852.lib.utilities.Translation2D;

public class CubicSpline extends Spline {
    private final double ax, bx, cx, dx, ay, by, cy, dy, x0, x1, dx0, dx1, y0, y1, dy0, dy1;

    public CubicSpline(Pose2D start, Pose2D finish) {
        // Set start and finish
        super(start, finish);

        // Set position coefficients
        x0 = start.getTranslation().getX();
        x1 = finish.getTranslation().getX();
        y0 = start.getTranslation().getY();
        y1 = finish.getTranslation().getY();

        // Set derivative coefficients
        double scale = 2 * start.distanceTo(finish);
        dx0 = start.getRotation().getCos() * scale;
        dx1 = finish.getRotation().getCos() * scale;
        dy0 = start.getRotation().getSin() * scale;
        dy1 = finish.getRotation().getSin() * scale;


        // Calculate equation coefficients
        ax = dx0 + dx1 + 2 * x0 - 2 * x1;
        bx = -2 * dx0 - dx1 - 3 * x0 + 3 * x1;
        cx = dx0;
        dx = x0;
        ay = dy0 + dy1 + 2 * y0 - 2 * y1;
        by = -2 * dy0 - dy1 - 3 * y0 + 3 * y1;
        cy = dy0;
        dy = y0;
    }

    @Override
    Translation2D evaluateFunction(double t) {
        return new Translation2D(ax * t * t * t + bx * t * t + cx * t + dx, ay * t * t * t + by * t * t + cy * t + dy);
    }

    @Override
    Translation2D evaluateDerivative(double t) {
        return new Translation2D(3 * ax * t * t + 2 * bx * t + cx, 3 * ay * t * t + 2 * by * t + cy);
    }

    @Override
    Rotation2D evaluateRotation(double t) {
        return new Rotation2D(evaluateDerivative(t).getX(), evaluateDerivative(t).getY());
    }

    @Override
    public double lengthLowerBound() {
        // Lower bound from distance between start and end
        return Math.hypot(x1 - x0, y1 - y0);
    }

    @Override
    public double lengthUpperBound() {
        // Upper bound from total distance traveled in x and y
        return Math.hypot(ax / 3 + bx / 2 + cx, ay / 3 + by / 2 + cy);
    }

    @Override
    Pose2D samplePath(double t) {
        return new Pose2D(evaluateFunction(t), evaluateRotation(t));
    }
}
