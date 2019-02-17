package frc.team852.lib.utils;

import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.path.utilities.Pose2D;
import frc.team852.lib.path.utilities.Translation2D;
import frc.team852.subsystem.Drivetrain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class PositionTracking implements Runnable {

    public static final double ONE_METER = 2868d;
    public static final double trackDistance = Drivetrain.trackDistance;  // 61.12 cm distance between wheel sides

    public static final boolean logging = true;
    public static final boolean pure = true;
    public static final boolean useGyro = true;


    public AtomicReference<Pose2D> currPose = new AtomicReference<>();
    public AtomicReference<Double> currX = new AtomicReference<>(0d);
    public AtomicReference<Double> currY = new AtomicReference<>(0d);
    public AtomicReference<Double> currAngle = new AtomicReference<>(0d);

    private double lastEncValue;
    private double lastGyroHeading;
    private StringBuilder log;


    public PositionTracking() {
        if (logging) {
            log = new StringBuilder();
        }
        reset();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Thread.sleep(10);

                double leftEnc = RobotMap.leftEncoder.get();
                double rightEnc = RobotMap.rightEncoder.get();
                double currEncValue = (leftEnc + rightEnc) / 2 / ONE_METER;
                double currGyroHeading;
                if (useGyro)
                    currGyroHeading = Math.toRadians(Robot.gyro.getAngle());
                else
                    currGyroHeading = (rightEnc - leftEnc) / 2 / ONE_METER / trackDistance;

                log.append(System.currentTimeMillis()).append(',')
                        //.append(OI.stick1.getY() / 2).append(',')
                        .append(currEncValue).append('\n');

                double dist = currEncValue - lastEncValue;
                double angle = currGyroHeading - lastGyroHeading;

                if (pure) {
                    double deltaX = 0;
                    double deltaY = 0;
                    double deltaAngle = 0;
                    if (angle == 0) {
                        deltaX = dist;
                        deltaY = 0;
                    } else {
                        double radius = dist / angle;
                        deltaX = radius * Math.sin(angle);
                        deltaY = radius * (1 - Math.cos(angle));

                        double oldAngle = currAngle.get();
                        double tempX = deltaX * Math.cos(oldAngle) - deltaY * Math.sin(oldAngle);
                        double tempY = deltaX * Math.sin(oldAngle) + deltaY * Math.cos(oldAngle);
                        deltaX = tempX;
                        deltaY = tempY;

                        deltaAngle = angle;
                    }
                    currX.accumulateAndGet(deltaX, Double::sum);
                    currY.accumulateAndGet(deltaY, Double::sum);
                    currAngle.accumulateAndGet(deltaAngle, Double::sum);
                } else {
                    Pose2D nextPose;
                    if (angle == 0) {
                        nextPose = new Pose2D(dist, 0);
                    } else {
                        double radius = dist / angle;
                        nextPose = new Pose2D(radius * Math.sin(angle), radius * (1 - Math.cos(angle)), angle);
                    }
                    currPose.accumulateAndGet(nextPose, Pose2D::compose);
                }

                lastEncValue = currEncValue;
                lastGyroHeading = currGyroHeading;
            }
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted position tracking.");
        }
        writeLog();
        System.out.println("Ended position tracking.");
    }

    public Pose2D getCurrentPose() {
        if (pure)
            return new Pose2D(currX.get(), currY.get(), currAngle.get());
        else
            return currPose.get();
    }

    public void reset(Pose2D pose) {
        currPose.set(pose);
        currX.set(pose.getTranslation().getX());
        currY.set(pose.getTranslation().getY());
        currAngle.set(pose.getRotation().getAngle());
        RobotMap.leftEncoder.reset();
        RobotMap.rightEncoder.reset();
    }

    public void reset(double x, double y, double angle) {
        currPose.set(new Pose2D(x, y, angle));
        currX.set(x);
        currY.set(y);
        currAngle.set(angle);
        RobotMap.leftEncoder.reset();
        RobotMap.rightEncoder.reset();
    }

    public void reset() {
        reset(0, 0, 0);
    }

    public void resetPosition(Translation2D translation) {
        if (pure)
            reset(new Pose2D(translation, currAngle.get()));
        else
            reset(new Pose2D(translation, currPose.get().getRotation()));
    }

    public void resetPosition(double x, double y) {
        if (pure)
            reset(x, y, currAngle.get());
        else
            reset(x, y, currPose.get().getRotation().getAngle());
    }

    private void writeLog() {
        File file = new File("/home/lvuser/driveLog.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(log.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
