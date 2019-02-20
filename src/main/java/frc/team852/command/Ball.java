package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.BallSubsystem;

public class Ball extends Command {
	private final BallSubsystem bs;
	public static boolean ballIn = true;
	
	public Ball() {
		requires(Robot.ballSubsystem);
		bs = Robot.ballSubsystem;
	}
	
	@Override
	protected void initialize() {
		ballIn = !ballIn;
		if(ballIn){
			bs.dropBall();
		}else{
			bs.grabBall();
		}
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}
}
