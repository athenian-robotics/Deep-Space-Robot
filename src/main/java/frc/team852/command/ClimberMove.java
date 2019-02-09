package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.ClimberSubsystem;

public class ClimberMove extends Command {
    private final ClimberSubsystem climber;

	/**
	 * @param speed the speed to move the climber at
	 * @param target the target location, in encoder ticks, to move the motor to
	 *               	//TODO convert to inches?
	 *               may go slightly past the encoder position
	 */

    //In case no speed is given, default to this
    public ClimberMove() {
      requires(Robot.climberSubsystem);
      climber = Robot.climberSubsystem;
    }

    @Override
    protected void initialize(){
      climber.resetEncoder();
      climber.setSetpoint(climber.getEncoderPos());
    }

    //Called when interrupted
    @Override
    protected void interrupted() {
      end();
    }

    //Reset encoders, reset motors, put everything back to where it was.
    @Override
    protected void end() {
      climber.disable();
      climber.resetEncoder();
      climber.stopMotors();
    }

    //Never stop, change this later once we are no longer using buttons to start and stop
    @Override
    protected boolean isFinished() {
        return false;
    }

    //Pass a speed through the motors, stop when done
    protected void execute() {
      if(!climber.getPIDController().isEnabled())
        climber.enable();
    }
}
