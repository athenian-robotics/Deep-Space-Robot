package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.BallSubsystem;

public class Ball extends Command {
	private final BallSubsystem bs;
	private long startTime;
	private boolean done = false;
	
	public Ball() {
		requires(Robot.ballSubsystem);
		bs = Robot.ballSubsystem;
	}
	@Override
	protected void execute(){
		if(startTime + 100 > System.currentTimeMillis()) {
			bs.grabBall();
			done=true;
			System.out.println("HERE-E2");
			
		}
	}
	
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		bs.dropBall();
		System.out.println("HERE-E1");
	}
	
	@Override
	protected boolean isFinished() {
		return done;
	}
}
