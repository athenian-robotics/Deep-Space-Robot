/**
 * A simple-as-possible subsystem tester for the SPARKMAX/NEO motor controller/motor dynamic duo.
 * @author Ezra Newman
 * @version 2019-01-19
 * @see frc.team852.commands.NEOTester
 **/
package frc.team852.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.commands.NEOTester;

	public class NEOTesterSub extends Subsystem {
	
	private SpeedControllerGroup neoMotors = new SpeedControllerGroup(RobotMap.neoTest);
	private double currEncoderValT;
	
	//TODO fix inversion motor & check encoder tick up/down
	
	public NEOTesterSub(){
		super();
		RobotMap.neoTest.setInverted(true);
		currEncoderValT = RobotMap.neoTest.getEncoder().getPosition();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new NEOTester(0.10));
	}
	
	public void setSpeed(double speed){
		this.neoMotors.set(speed);
	}
	
	public void stopMotors(){
		this.neoMotors.stopMotor();
	}
	
	public double getSpeed(){
		return this.neoMotors.get();
	}
	
	public double getEncoderPos(){
		return RobotMap.neoTest.getEncoder().getPosition();
	}
	
	public void resetEncoders(){ //TODO implement a one that works, fix zeros bug
		currEncoderValT = RobotMap.neoTest.getEncoder().getPosition();

	}
	
}
