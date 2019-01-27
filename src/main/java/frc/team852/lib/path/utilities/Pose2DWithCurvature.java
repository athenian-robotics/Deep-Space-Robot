/**
 * Data type that implements CSVWritable
 * Contains a Pose2D (Translation2D and Rotation2D) along with curvature and change in curvature (dcurvature_ds)
 */

package frc.team852.lib.utilities;

public class Pose2DWithCurvature implements CSVWritable {
    private final Pose2D pose;
    private final double curvature;
    private final double dcurvature_ds;

    public Pose2DWithCurvature(Pose2D pose, double curvature, double dcurvature_ds) {
        this.pose = pose;
        this.curvature = curvature;
        this.dcurvature_ds = dcurvature_ds;
    }

    public double distanceTo(Translation2D other) {
        return getTranslation().distanceTo(other);
    }

    public Pose2D getPose() {
        return pose;
    }

    public Translation2D getTranslation() {
        return pose.getTranslation();
    }

    public Rotation2D getRotation() {
        return pose.getRotation();
    }

    public double getCurvature() {
        return curvature;
    }

    public double getDCurvatureDs() {
        return dcurvature_ds;
    }

    @Override
    public String toCSV() {
        return String.format("%s,%.6f,%.6f", pose.toCSV(), curvature, dcurvature_ds);
    }
}