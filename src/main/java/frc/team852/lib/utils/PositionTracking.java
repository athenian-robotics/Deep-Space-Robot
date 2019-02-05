package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.Timer;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.path.utilities.Pose2D;

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
        while (!Thread.interrupted()) {
            Timer.delay(.005);

            long currTimestamp = System.currentTimeMillis();
            double left = -Robot.drivetrain.getLeft();
            double right = Robot.drivetrain.getRight();
            double currEncValue = (left + right) / 2;
            //double currGyroHeading = (right - left) / 2;
            double currGyroHeading = Robot.gyro.getFusedHeading();

            if (lastTimestamp == 0 || Robot.gyro.isCalibrating()) {
                lastTimestamp = currTimestamp;
                lastEncValue = currEncValue;
                lastGyroHeading = currGyroHeading;
                continue;
            }


            double dt = (currTimestamp - lastTimestamp) / 1000d;

            double denc = currEncValue - lastEncValue;
            double tpr = getTicksPerRevolution();
            double dist = denc / tpr * wheelCircumference;

            double angle = currGyroHeading - lastGyroHeading;

            Pose2D nextPose;
            if (angle == 0) {
                nextPose = new Pose2D(dist, 0);
            }
            else {
                double radius = dist / angle;
                nextPose = new Pose2D(radius * Math.sin(angle), radius * (1 - Math.cos(angle)), angle);
            }
            currPose.accumulateAndGet(nextPose, Pose2D::compose);

            lastTimestamp = currTimestamp;
            lastEncValue = currEncValue;
            lastGyroHeading = currGyroHeading;
        }
        System.out.println("Imma die");
    }

    private void reset() {
        currPose.set(new Pose2D());
        lastTimestamp = 0;
    }

    private double getTicksPerRevolution() {
        return (Robot.gearstate == RobotMap.SLOW) ? ticksPerRevolution_HG : ticksPerRevolution_LG;
    }
}
