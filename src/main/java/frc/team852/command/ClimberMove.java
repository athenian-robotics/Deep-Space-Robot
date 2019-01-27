package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;

public class ClimberMove extends Command {
    final double speed;

    //Allow for speed to be passed through the motors
    public ClimberMove(double speed) {
        super();
        requires(Robot.climberSubsystem);
        this.speed = speed;
    }
    //In case no speed is given, default to this
    public ClimberMove() {
        this(1);
    }

    //If interrupted, run end();
    protected void interrupted() {
        end();
    }

    //Reset encoders, reset motors, put everything back to where it was.
    protected void end() {
        RobotMap.climberMotor.resetEncoder();
        RobotMap.climberSubsystem.stopMotors();
    }

    //Never stop, change this later once we are no longer using buttons to start and stop
    protected boolean isFinished() {
        return false;
    }

    //Pass a speed through the motors
    //TODO Implement encoders to know where the climber is relative to the ground or whatever you want it to be relative to.
    protected void execute() {
        RobotMap.climberMotor.set(speed);
    }
}
