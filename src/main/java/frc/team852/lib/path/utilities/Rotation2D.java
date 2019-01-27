/**
 * Data type used for specifying heading/rotation of robot and methods to manipulate it, implements Rotation2D
 */
package frc.team852.lib.path.utilities;

public class Rotation2D implements CSVWritable {

    //the angle is stored as a cos and sin rather than the angle measure itself
    private final double cos;
    private final double sin;

    public Rotation2D() {
        this.cos = 1;
        this.sin = 0;
    }

    public Rotation2D(double angle) {
        this.cos = Math.cos(angle);
        this.sin = Math.sin(angle);
    }

    public Rotation2D(double cos, double sin) {
        double length = Math.hypot(cos, sin);
        this.cos = cos / length;
        this.sin = sin / length;
    }

    public Rotation2D(Rotation2D other) {
        this.cos = other.cos;
        this.sin = other.sin;
    }

    public double getCos() {
        return cos;
    }

    public double getSin() {
        return sin;
    }

    public double getAngle() {
        return Math.atan2(sin, cos);
    }

    //Calculates distance between two angles
    public double angleTo(Rotation2D other) {
        return inverse().rotate(other.inverse()).getAngle();
    }

    public Rotation2D inverse() {
        return new Rotation2D(cos, -sin);
    }

    //Adds two Rotation2D objects together
    public Rotation2D rotate(Rotation2D other) {
        Rotation2D o = other.inverse();
        return new Rotation2D(cos * o.cos - sin * o.sin, cos * o.sin + sin * o.cos);
    }

    public Rotation2D rotate(double a) {
        return rotate(new Rotation2D(a));
    }

    public Rotation2D scale(double s) {
        return new Rotation2D(s * getAngle());
    }

    public Rotation2D interpolate(Rotation2D other, double t) {
        return rotate(t * angleTo(other));
    }

    @Override
    public String toCSV() {
        return String.format("%.6f,%.6f", cos, sin);
    }
}
