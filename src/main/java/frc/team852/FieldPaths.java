package frc.team852;

import frc.team852.lib.path.geometry.PathGenerator;
import frc.team852.lib.path.utilities.CSVEditor;
import frc.team852.lib.path.utilities.Pose2D;
import frc.team852.lib.path.utilities.Trajectory;

import java.io.IOException;
import java.util.List;

public class FieldPaths {
    public static List<Pose2D> testPath;
    public static PathGenerator testGen;

    public static void genPaths() {
        Trajectory<Pose2D> test = new Trajectory<>();
        test.add(new Pose2D(0, 0, 0));
        test.add(new Pose2D(2, 0, 0));
        test.add(new Pose2D(4, 2, Math.PI / 2));
        //test.add(new Pose2D(0, 2, Math.PI / 2));
        testGen = new PathGenerator(test);
        testPath = testGen.generatePoints();
    }

    public static void main(String[] args) {
        genPaths();
        try {
            CSVEditor.setName("field_test.csv");
            CSVEditor.dumpCSV(testGen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
