package frc.team852;

import frc.team852.lib.path.geometry.PathGenerator;
import frc.team852.lib.path.utilities.Pose2D;
import frc.team852.lib.path.utilities.Trajectory;

import java.util.List;

public class FieldPaths {
    public static List<Pose2D> testPath;

    public static void genPaths() {
        Trajectory<Pose2D> test = new Trajectory<>();
        test.add(new Pose2D(0, 0, 0));
        test.add(new Pose2D(0, 2, 0));
        //test.add(new Pose2D(0, 2, Math.PI / 2));
        testPath = new PathGenerator(test).generatePoints();
    }
}
