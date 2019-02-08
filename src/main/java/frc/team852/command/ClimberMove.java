package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.ClimberSubsystem;

public class ClimberMove extends Command {
    final double speed;
    final double target;
    final ClimberSubsystem climber = Robot.climberSubsystem;
	
	/**
	 * @param speed the speed to move the climber at
	 * @param target the target location, in encoder ticks, to move the motor to
	 *               	//TODO convert to inches?
	 *               may go slightly past the encoder position
	 */
    public ClimberMove(double speed, double target) {
        super();
        requires(Robot.climberSubsystem);
        this.speed = speed;
        this.target = target;
    }
    //In case no speed is given, default to this
    public ClimberMove() {
        this(1, 70);
    }

    //Called when interrupted
    protected void interrupted() {
        end();
    }

    //Reset encoders, reset motors, put everything back to where it was.
    protected void end() {
        climber.resetEncoder();
        climber.stopMotors();
        //TODO return to hold command
    }

    //Never stop, change this later once we are no longer using buttons to start and stop
    protected boolean isFinished() {
        return false;
    }

    //Pass a speed through the motors, stop when done
    protected void execute() {
    	if (climber.getEncoderPos() < target)
        	climber.setSpeed(speed);
    	else
    		end();
    }
}
