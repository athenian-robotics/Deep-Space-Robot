/**
 * Abstract class extended by CubicSpline
 */
package frc.team852.lib.geometry;

import frc.team852.lib.utilities.Pose2D;
import frc.team852.lib.utilities.Rotation2D;
import frc.team852.lib.utilities.Translation2D;

public abstract class Spline {
    private Pose2D start;
    private Pose2D finish;

    public Spline(Pose2D start, Pose2D finish) {
        this.start = start;
        this.finish = finish;
    }

    public final Pose2D getStart() {
        return start;
    }

    public final Pose2D getFinish() {
        return finish;
    }

    abstract Translation2D evaluateFunction(double t);

    abstract Translation2D evaluateDerivative(double t);

    abstract Rotation2D evaluateRotation(double t);

    public abstract double lengthLowerBound();

    public abstract double lengthUpperBound();

    abstract Pose2D samplePath(double t);
}
