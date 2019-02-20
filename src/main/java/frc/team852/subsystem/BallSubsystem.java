package frc.team852.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.Ball;

public class BallSubsystem extends Subsystem {
	private final DoubleSolenoid sucker;
	public BallSubsystem(){
		sucker = RobotMap.sucker;
	}
	
	@Override
	protected void initDefaultCommand(){}
	
	public void grabBall(){
		sucker.set(DoubleSolenoid.Value.kForward);
	}
	
	public void dropBall(){
		sucker.set(DoubleSolenoid.Value.kReverse);
	}
}
