package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.Drivetrain;

//TODO god only knows if this works.

public class DriveArcVelocity extends Command {
    // https://www.desmos.com/calculator/d7p5kcfj7j

    public static final double maxAcceleration = 1; // TODO idk what this should be
    public static final double decelRate = Math.sqrt(2 * maxAcceleration);  // Coefficient used in deceleration phase
    public static final double decelThreshold = 0.5 / maxAcceleration;  // starting distance for deceleration relative to target distance

    // TODO idk what these should be
    public static final double marginVelocity = 0.1;  // error margin for velocity in meters/second
    public static final double marginDistance = 0.1;  // error margin for distance in meters

    public static final int STATE_ACCEL = 0;  // Robot still accelerating to target speed
    public static final int STATE_CONST = 1;  // Robot driving at target speed
    public static final int STATE_DECEL = 2;  // Robot decelerating to a halt near target distance
    public static final int STATE_ENDED = 3;  // Robot has reached target distance and halted

    private final DriveVelocity driveVelocity;
    private final double targetDistance;
    private final double targetVelocity;
    private final double targetCurvature;
    
    private long startTime;
    private double startDistance;
    public double startHeading;
    
    private static final double trackCoeff = Drivetrain.trackDistance; //TODO Fix if needed
    private int state;


    public DriveArcVelocity(double targetDistance, double targetCurvature, double targetVelocity) {
    	
        driveVelocity = new DriveVelocity();
        this.targetDistance = Math.max(0, targetDistance);
        this.targetVelocity = Math.max(0, targetVelocity);
        this.targetCurvature = targetCurvature;
    }
    
    public DriveArcVelocity(double targetDistance, double targetCurvature){
    	this(targetDistance, targetCurvature, 0.3);
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

        // Calculate distance and heading relative to start
        double distanceTraveled = Robot.drivetrain.getDistance() - startDistance;
        double headingError = Robot.gyro.getAngle() - startHeading - (targetCurvature / trackCoeff) * distanceTraveled;


        // Update drivetrain values
        switch (state) {
            case STATE_ACCEL:
                // Ramp up velocity over time at the rate given by maxAcceleration
                forwardVelocity = maxAcceleration * (System.currentTimeMillis() - startTime) / 1000d;
                // Hold it constant once target velocity reached
                forwardVelocity = Math.max(targetVelocity, forwardVelocity);
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
