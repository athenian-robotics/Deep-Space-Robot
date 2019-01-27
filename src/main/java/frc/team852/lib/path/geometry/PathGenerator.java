/**
 * Generates splines between each waypoint (using CubicSpline class) and generates equidistant points on the spline
 */
package frc.team852.lib.geometry;

import frc.team852.lib.utilities.CSVWritable;
import frc.team852.lib.utilities.Pose2D;
import frc.team852.lib.utilities.Trajectory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PathGenerator implements CSVWritable {
    private static final int maxSamples = 200;
    public static final double defaultIncrement = 0.1;
    public static final double minIncrement = 0.001;

    private final Trajectory<Pose2D> trajectory;
    private final List<Pose2D> points = new ArrayList<>();

    private double distIncrement = defaultIncrement;


    public PathGenerator(Pose2D start, Pose2D finish) {
        trajectory = new Trajectory<Pose2D>();
        trajectory.add(start);
        trajectory.add(finish);
    }

    public PathGenerator(List<Pose2D> trajectory) {
        assert(trajectory.size() >= 2);
        this.trajectory = new Trajectory<>(trajectory);
    }

    public PathGenerator(Pose2D first, Pose2D second, Pose2D ... rest) {
        trajectory = new Trajectory<>(Arrays.asList(rest));
        trajectory.add(0, first);
        trajectory.add(1, second);
    }

    public PathGenerator(Trajectory<Pose2D> trajectory) {
        assert(trajectory.size() >= 2);
        this.trajectory = trajectory;
    }


    Pose2D getStart() {
        return trajectory.get(0);
    }

    Pose2D getEnd() {
        return trajectory.get(trajectory.size() - 1);
    }

    public void setDistIncrement(double distIncrement) {
        this.distIncrement = Math.max(minIncrement, distIncrement);
    }

    public boolean generated() {
        return (points.size() > 0);
    }


    public List<Pose2D> generatePoints() {
        // Stop if already generated
        if (generated()) {
            return points;
        }

        // Connect trajectory with splines
        List<Spline> splines = new ArrayList<>();
        for (int i = 0; i < trajectory.size() - 1; i++) {
            splines.add(new CubicSpline(trajectory.get(i), trajectory.get(i + 1)));
        }

        // Join the parametrized splines into a single list
        List<Pose2D> points = new ArrayList<>();

        // Parameterize splines
        for (Spline spline : splines) {
            // Twice as many samples as estimated by the upper bound should be plenty accurate
            int numSamples = 2 * (int) (spline.lengthUpperBound() / distIncrement);

            // It needs at least two samples, but anything over numSamples is excessive
            numSamples = Math.max(1, Math.min(maxSamples, numSamples));


            // Generate sample points
            Pose2D[] samples = new Pose2D[numSamples];
            double denom = numSamples - 1;

            for (int i = 0; i < numSamples; i++) {
                samples[i] = spline.samplePath(i / denom);
            }


            // Calculate distances for sample points
            double[] distances = new double[numSamples];
            distances[0] = 0;
            double maxStepDist = 0;

            for (int i = 1; i < numSamples; i++) {
                distances[i] = distances[i - 1] + samples[i - 1].distanceTo(samples[i]);
                maxStepDist = Math.max(maxStepDist, samples[i - 1].distanceTo(samples[i]));
            }

            // Find largest number of indices between two intervals
            int maxStep = (int) (maxStepDist / distIncrement) + 1;


            // Step along spline by distance intervals of distIncrement
            int numIntervals = (int) (distances[numSamples - 1] / distIncrement);
            Pose2D[] intervals = new Pose2D[numIntervals];
            intervals[0] = samples[0];

            int index = 0;
            for (int i = 1; i < numIntervals; i++) {
                // Find the bounding samples using a linear search
                while (distances[index + 1] < i * distIncrement) {
                    index++;
                }

                if (index >= numSamples)
                    index = numSamples - 1;

                // Interpolate based on distance
                intervals[i] = samples[index].interpolate(samples[index + 1],
                        (i * distIncrement - distances[index]) / (distances[index + 1] - distances[index]));
            }

            // Join intervals into a single list
            Collections.addAll(points, intervals);
        }

        return points;
    }


    @Override
    public String toCSV() {
        List<Pose2D> points = generatePoints();
        StringBuilder res = new StringBuilder();
        for (Pose2D p : points) {
            res.append(p.toCSV()).append("\n");
        }
        return res.toString();
    }
}
