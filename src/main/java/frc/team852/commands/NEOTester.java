package frc.team852.commands;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystems.NEOTesterSub;

public class NEOTester extends Command {
	private double speed;
	private final NEOTesterSub neoTesterSub = Robot.neoTesterSub;
	
	//TODO Implement speed
	public NEOTester(double speed) {
		requires(Robot.neoTesterSub);
		this.speed = speed;
	}
	
	@Override
	protected boolean isFinished() {
		//this.currentDistance = RobotMap.elevatorDistanceSensor.get();
		return false;
	}
	
	@Override
	protected void end() {
		neoTesterSub.stopMotors();
		//TODO Return control to hold command
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
	@Override
	protected void execute() {
		neoTesterSub.setSpeed(OI.stick1.getY());
		if(neoTesterSub.getEncoderPos()==0 || true){
			System.out.println(neoTesterSub.getEncoderPos());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(OI.stick1.getTrigger()) neoTesterSub.resetEncoders();
	}
}
