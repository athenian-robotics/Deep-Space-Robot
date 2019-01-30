package frc.team852.command;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.path.utilities.Pose2D;
import frc.team852.subsystem.Drivetrain;

import java.util.List;

/**
 * A naive implementation of driving along a sampled path.
 * @author Vincent Pisani
 */
public class DrivePathBasic extends Command {

    private int logCounter;
    private Drivetrain dt = Robot.drivetrain;
    private AHRS gyro = Robot.gyro;

    private List<Pose2D> points;  // list of poses to follow
    private Pose2D prev;  // starting pose
    private Pose2D curr;  // ending pose
    private int index;  // index of ending pose

    private double encoderStart;  // encoder value at start of execution (to emulate an encoder reset)
    private double distCovered;  // distance covered from previous steps
    private double currDist;  // distance for current step

    private double leftSpeed;  // speed for left side
    private double rightSpeed;  // speed for right side

    private static final double maxSpeed = 0.2;
    private static final double ticksPerRevolution = 277.2;
    private static final double distanceUnits = 0.1;
    // Ticks / revolution = 277.2 high gear, 630 low gear

    public DrivePathBasic(List<Pose2D> path) {
        if (path.size() < 2)
            throw new IllegalArgumentException("Path length must be at least 2");

        points = path;

        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        // Make sure drivetrain is in high gear
        if (dt.getGearing() == RobotMap.HIGH_GEAR) {
            dt.setGearbox(RobotMap.HIGH_GEAR);
            System.out.println("IN HIGH GEAR");
        }
        // Emulate an encoder reset
        encoderStart = 0;
        dt.resetEncoders();
        encoderStart = getEncoderDist();

        prev = null;
        curr = points.get(0);
        index = 0;
        distCovered = 0;
        currDist = 0;
        nextStep();

        logCounter = 0;
    }

    @Override
    protected boolean isFinished() {
        return index >= points.size();
    }

    @Override
    protected void end() {
        dt.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void execute() {
        double dist = getEncoderDist() - distCovered;

        logCounter++;
        if (logCounter >= 10) {
            logCounter = 0;
            System.out.printf("Distance: %f\n", dist);
        }

        if (dist > currDist) {
            nextStep();
            return;
        }

        dt.drive(leftSpeed, rightSpeed);
    }

    private double getEncoderDist() {
        return (dt.getLeft() - dt.getRight() - encoderStart) / 2 / ticksPerRevolution / distanceUnits;
    }

    private void nextStep() {
        System.out.println("Next step");
        index++;
        if (index >= points.size())
            return;
        System.out.printf("Index: %d\n", index);

        prev = curr;
        curr = points.get(index);
        System.out.printf("Prev pose: %s\n", prev);
        System.out.printf("Curr pose: %s\n", curr);

        distCovered += currDist;
        currDist = curr.distanceTo(prev);
        System.out.printf("Distance progress: %f\n", distCovered);
        System.out.printf("Curr distance: %f\n", currDist);
        computeSpeeds();
    }

    private void computeSpeeds() {
        // TODO this is an inaccurate hack, please fix once Pose2DWithCurvature
        System.out.println("Compute speeds");
        double angle = prev.netAngleTo(curr);
        System.out.printf("Angle: %f\n", angle);

        // Compute the curvature of an arc with angle angle and length currDist

        double curvature = angle / currDist / 2;
        double mag = maxSpeed / (1 + Math.abs(curvature));
        leftSpeed = mag * (1 - curvature);
        rightSpeed = mag * (1 + curvature);
        System.out.printf("Curvature: %f\n", curvature);
        System.out.printf("Magnitude: %f\n", mag);
        System.out.printf("Speeds: (%f, %f)\n", leftSpeed, rightSpeed);
    }
}
