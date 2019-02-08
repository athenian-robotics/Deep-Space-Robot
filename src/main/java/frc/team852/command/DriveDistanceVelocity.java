package frc.team852.command;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team852.Robot;

import java.rmi.UnexpectedException;

public class DriveDistanceVelocity extends Command {
    // https://www.desmos.com/calculator/d7p5kcfj7j

    public static final double maxAcceleration = 1; // TODO idk what this should be
    public static final double decelThreshold = 0.5 / maxAcceleration;  // starting distance for deceleration relative to target distance

    // TODO idk what these should be
    public static final double marginVelocity = 0.1;  // error margin for velocity in meters/second
    public static final double marginDistance = 0.1;  // error margin for distance in meters

    public static final int STATE_ACCEL = 0;  // Robot still accelerating to target speed
    public static final int STATE_CONST = 1;  // Robot driving at target speed
    public static final int STATE_DECEL = 2;  // Robot decelerating to a halt near target distance
    public static final int STATE_ENDED = 3;  // Robot has reached target distance and halted

    private DriveVelocity driveVelocity;
    private double targetDistance;
    private double targetSpeed;

    private double startTime;
    private double startDistance;
    public double startHeading;

    private int state;

    private ShuffleboardTab tab = Shuffleboard.getTab("Drive");
    private NetworkTableEntry kpEntry = tab.add("DriveDistanceVelocity Kp", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private NetworkTableEntry kiEntry = tab.add("DriveDistanceVelocity Ki", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private NetworkTableEntry kdEntry = tab.add("DriveDistanceVelocity Kd", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private NetworkTableEntry kfEntry = tab.add("DriveDistanceVelocity Kf", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();


    public DriveDistanceVelocity(double targetDistance, double targetSpeed) {

        driveVelocity = new DriveVelocity(0, 0, 0, 0);
        this.targetDistance = Math.max(0, targetDistance);
        this.targetSpeed = Math.max(0, targetSpeed);
    }

    public DriveDistanceVelocity(double targetDistance) {
        this(targetDistance, 0.5);
    }

    @Override
    protected void initialize() {
        startTime = System.currentTimeMillis();
        startDistance = Robot.drivetrain.getDistance();
        startHeading = Robot.gyro.getAngle();
        driveVelocity.start();
    }

    @Override
    protected void end() {
        driveVelocity.end();
    }

    @Override
    protected void execute() {
        double forwardVelocity = 0;
        double angularVelocity = 0;

        double distanceTraveled = Robot.drivetrain.getDistance() - startDistance;
        double headingError = Robot.gyro.getAngle() - startHeading;

        // Update PID controller with values from dashboard
        driveVelocity.setAll(
                kpEntry.getDouble(0),
                kiEntry.getDouble(0),
                kdEntry.getDouble(0),
                kfEntry.getDouble(0));

        // Update drivetrain values
        switch (state) {
            case STATE_ACCEL:
                //forwardVelocity = maxAcceleration * (System.currentTimeMillis() - startTime) / 1000;
                break;
            case STATE_CONST:
                //
                break;
            case STATE_DECEL:
                //
                break;
        }
        driveVelocity.setSetpoints(
                forwardVelocity - angularVelocity,
                forwardVelocity + angularVelocity);

        // Check for state transitions
        switch (state) {
            case STATE_ACCEL:
                if (driveVelocity.getAbsError() < marginVelocity
                        || targetDistance - distanceTraveled < decelThreshold)
                    state = STATE_CONST;
                break;
            case STATE_CONST:
                if (targetDistance - distanceTraveled < decelThreshold)
                    state = STATE_DECEL;
                break;
            case STATE_DECEL:
                if (Math.abs(targetDistance - distanceTraveled) < marginDistance
                        || driveVelocity.getAbsError() < marginVelocity)
                    state = STATE_ENDED;
                break;
        }
    }

    @Override
    protected boolean isFinished() {
        return state == STATE_ENDED;
    }

    @Override
    protected void interrupted() {
        end();
    }
}
