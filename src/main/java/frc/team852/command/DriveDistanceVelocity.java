package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;

public class DriveDistanceVelocity extends Command {
    // https://www.desmos.com/calculator/d7p5kcfj7j

    public static final double maxAcceleration = 0.5; // TODO idk what this should be
    public static final double decelRate = Math.sqrt(2 * maxAcceleration);  // Coefficient used in deceleration phase
    public static final double decelThreshold = 0.5 / maxAcceleration;  // starting distance for deceleration relative to target distance

    // TODO idk what these should be
    public static final double marginVelocity = 0.02;  // error margin for velocity in meters/second
    public static final double marginDistance = 0.02;  // error margin for distance in meters

    public static final int STATE_ACCEL = 0;  // Robot still accelerating to target speed
    public static final int STATE_CONST = 1;  // Robot driving at target speed
    public static final int STATE_DECEL = 2;  // Robot decelerating to a halt near target distance
    public static final int STATE_ENDED = 3;  // Robot has reached target distance and halted

    private final DriveVelocity driveVelocity;
    private final double targetDistance;
    private final double targetVelocity;

    private long startTime;
    private double startDistance;
    public double startHeading;

    private int state;


    public DriveDistanceVelocity(double targetDistance, double targetVelocity) {

        driveVelocity = new DriveVelocity();
        this.targetDistance = Math.max(0, targetDistance);
        this.targetVelocity = Math.max(0, targetVelocity);
    }

    public DriveDistanceVelocity(double targetDistance) {
        this(targetDistance, 0.5);
    }

    @Override
    protected void initialize() {
        startTime = 0;
        startDistance = Robot.drivetrain.getDistance();
        startHeading = Robot.gyro.getAngle();
        state = STATE_ACCEL;
        driveVelocity.start();
    }

    @Override
    protected void end() {
        driveVelocity.cancel();
    }

    @Override
    protected void execute() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
            return;
        }

        double forwardVelocity = 0;
        double angularVelocity = 0;

        // Calculate distance and heading relative to start
        double distanceTraveled = Robot.drivetrain.getDistance() - startDistance;
        double headingError = Robot.gyro.getAngle() - startHeading;
        headingError *= 0;

        // Update PID controller with values from dashboard

        //TODO: Duplicate code (fix)
        // Update drivetrain values
        switch (state) {
            case STATE_ACCEL:
                // Ramp up velocity over time at the rate given by maxAcceleration
                forwardVelocity = maxAcceleration * (System.currentTimeMillis() - startTime) / 1000d;
                // Hold it constant once target velocity reached
                forwardVelocity = Math.min(targetVelocity, forwardVelocity);
                break;
            case STATE_CONST:
                // Keep velocity at target velocity
                forwardVelocity = targetVelocity;
                break;
            case STATE_DECEL:
                // Ramp down velocity over distance as a function of the offset from the target distance
                double distanceOffset = targetDistance - distanceTraveled;
                // Velocity determined by square root of error from target distance, scaled by a deceleration constant
                // If overshoot, go backwards with negative velocity (accomplished with copySign)
                forwardVelocity = decelRate * Math.copySign(Math.sqrt(Math.abs(distanceOffset)), distanceOffset);
                break;
        }
        System.out.println(forwardVelocity);

        // Cheap proportional angle correction - TODO replace with PID if it doesn't work - also use trackDistance somewhere
        angularVelocity = -headingError;

        driveVelocity.setSetpoints(
                forwardVelocity * (1 - angularVelocity),
                forwardVelocity * (1 + angularVelocity));


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
                if (Math.abs(targetDistance - distanceTraveled) < marginDistance)
                        //|| driveVelocity.getAbsError() < marginVelocity)
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
