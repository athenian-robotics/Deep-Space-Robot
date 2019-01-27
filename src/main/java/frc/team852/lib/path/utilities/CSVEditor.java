/**
 * Class to write any CSVWritable objects to CSV
 * This is the main file; run the main() method to generate the path and save in CSV
 */
package frc.team852.lib.utilities;

import frc.team852.lib.geometry.PathGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class CSVEditor {

    private static final String systemPath; // from root to generated_path, no file
    private static final String workingPath = "/src/main/java/frc/team852/lib/utilities/generated_path/";
    private static String filename; //name
    private static String relativePath; // relative path
    private static String absPath; // absolute path


    static {
        // set default static file name as test (so it doesn't break) and save it to something
        filename = "test.csv";
        relativePath = workingPath + filename;
        systemPath = (new File("./")).getAbsoluteFile() + workingPath;
        absPath = systemPath + filename;
    }


    public static void dumpCSV(CSVWritable writable) throws IOException {
        System.out.println(absPath);
        FileWriter file = new FileWriter(absPath);

        try {
            file.write(writable.toCSV());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }

    public static void setName(String name) {
        filename = name;
        relativePath = workingPath + filename;
        absPath = systemPath + filename;
    }

    public String filename() {
        return filename;
    }

    public String absolutePath() {
        return absPath;
    }

    public static void main(String[] args) throws IOException {

        CSVEditor.setName("path_test.csv");

        Trajectory<Pose2D> trajectory = new Trajectory<>();
        trajectory.add(new Pose2D(0, 0));
        trajectory.add(new Pose2D(2,0));
        trajectory.add(new Pose2D(4, 3));

        PathGenerator path = new PathGenerator(trajectory);
        path.setDistIncrement(0.05);

        System.out.println(path.toCSV());

        CSVEditor.dumpCSV(path);


    }
}

