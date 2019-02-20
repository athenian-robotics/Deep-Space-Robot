package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.BallSubsystem;

public class Ball extends Command {
	private final BallSubsystem bs;
	private boolean ballIn = false;
	
	public Ball() {
		requires(Robot.ballSubsystem);
		bs = Robot.ballSubsystem;
	}
	
	@Override
	protected void initialize() {
		bs.dropBall();
		bs.grabBall();
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}
}
