/**
 * Data type to keep track of location in an xy-plane, implements Translation2D
 */
package frc.team852.lib.utilities;

public class Translation2D implements CSVWritable {


    private final double x;
    private final double y;

    public Translation2D() {
        this.x = 0;
        this.y = 0;
    }

    public Translation2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Translation2D(Translation2D other) {
        this.x = other.x;
        this.y = other.y;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    //Calculate distance between two Translation2D objects
    public double distanceTo(Translation2D other) {
        return Math.hypot(x - other.x, y - other.y);
    }

    public Translation2D interpolate(Translation2D other, double t) {
        return new Translation2D(x * (1 - t) + other.x * t, y * (1 - t) + other.y * t);
    }

    @Override
    public String toCSV() {
        return String.format("%.6f,%.6f", x, y);
    }

}
