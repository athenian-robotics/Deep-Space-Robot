package frc.team852.lib.utils;

import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.path.utilities.Pose2D;
import frc.team852.lib.path.utilities.Rotation2D;

import java.util.concurrent.atomic.AtomicReference;

public class PositionTracking implements Runnable {

    private static final double ticksPerRevolution_HG = 277.2;
    private static final double ticksPerRevolution_LG = 630;
    private static final double wheelCircumference = .1524 * Math.PI; // 15.24 cm wheel diameter

    public AtomicReference<Pose2D> currPose = new AtomicReference<>();
    private double lastEncValue;
    private double lastGyroHeading;
    private long lastTimestamp;


    public PositionTracking(Pose2D pose) {
        currPose.set(pose);
        lastTimestamp = 0;
    }

    public PositionTracking() {
        this(new Pose2D());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                long currTimestamp = System.currentTimeMillis();
                double currEncValue = (Robot.drivetrain.getRight() - Robot.drivetrain.getLeft()) / 2;
                double currGyroHeading = Robot.gyro.getFusedHeading();

                if (lastTimestamp == 0 || Robot.gyro.isCalibrating()) {
                    lastTimestamp = currTimestamp;
                    lastEncValue = currEncValue;
                    lastGyroHeading = currGyroHeading;
                    return;
                }


                double dt = (currTimestamp - lastTimestamp) / 1000d;

                double denc = currEncValue - lastEncValue;
                double tpr = getTicksPerRevolution();
                double dist = denc / tpr * wheelCircumference;

                double angle = currGyroHeading - lastGyroHeading;

                double radius = dist / angle;

                Pose2D nextPose = new Pose2D(radius * (1 - Math.cos(angle)), radius * Math.sin(angle));
                currPose.accumulateAndGet(nextPose, Pose2D::compose);

                lastTimestamp = currTimestamp;
                lastEncValue = currEncValue;
                lastGyroHeading = currGyroHeading;

                Thread.sleep(5);
            }
        }
        catch (InterruptedException e) {
            System.out.println("PositionTracking thread interrupted unexpectedly...");
        }
    }

    private void reset() {
        currPose.set(new Pose2D());
        lastTimestamp = 0;
    }

    private double getTicksPerRevolution() {
        return (Robot.gearstate == RobotMap.HIGH_GEAR) ? ticksPerRevolution_HG : ticksPerRevolution_LG;
    }
}
