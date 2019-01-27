/**
 * Data type that implements CSVWritable
 * Describes "pose" with Translation2D and Rotation2D objects
 */
package frc.team852.lib.path.utilities;

public class Pose2D implements CSVWritable {

    private final Translation2D translation;
    private final Rotation2D rotation;

    public Pose2D() {
        this.translation = new Translation2D();
        this.rotation = new Rotation2D();
    }

    public Pose2D(double x, double y) {
        this.translation = new Translation2D(x, y);
        this.rotation = new Rotation2D();
    }

    public Pose2D(Translation2D translation) {
        this.translation = translation;
        this.rotation = new Rotation2D();
    }

    public Pose2D(double x, double y, double angle) {
        this.translation = new Translation2D(x, y);
        this.rotation = new Rotation2D(angle);
    }

    public Pose2D(double x, double y, Rotation2D rotation) {
        this.translation = new Translation2D(x, y);
        this.rotation = rotation;
    }

    public Pose2D(Translation2D translation, double angle) {
        this.translation = translation;
        this.rotation = new Rotation2D(angle);
    }

    public Pose2D(Translation2D translation, Rotation2D rotation) {
        this.translation = translation;
        this.rotation = rotation;
    }

    public Pose2D(final Pose2D other) {
        translation = new Translation2D(other.translation);
        rotation = new Rotation2D(other.rotation);
    }

    //Calculate distance between two Pose2D objects
    public double distanceTo(Pose2D other) {
        return translation.distanceTo(other.translation);
    }

    public Pose2D interpolate(Pose2D other, double t) {
        return new Pose2D(translation.interpolate(other.translation, t), rotation.interpolate(other.rotation, t));
    }

    public Pose2D getPose() {
        return null;
    }

    public Rotation2D getRotation() {
        return null;
    }

    public Translation2D getTranslation() {
        return null;
    }

    public String toCSV() {
        return String.format("%s,%s", translation.toCSV(), rotation.toCSV());
    }
}
