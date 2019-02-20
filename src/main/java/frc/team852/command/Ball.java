package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.BallSubsystem;

public class Ball extends Command {
	private final BallSubsystem bs;
	private static boolean ballIn = false;
	
	public Ball() {
		requires(Robot.ballSubsystem);
		bs = Robot.ballSubsystem;
	}
	
	@Override
	protected void initialize() {
		if (ballIn) {
			bs.grabBall();
		} else {
			bs.dropBall();
		}
		ballIn = !ballIn;
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}
}
