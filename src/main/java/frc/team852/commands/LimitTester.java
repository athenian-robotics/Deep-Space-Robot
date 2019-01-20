/**
 * A simple-as-possible command tester for limit switches. intended as a base for future use.
 * @author Ezra Newman
 * @version 2019-01-19
 * @see frc.team852.subsystems.LimitTesterSub
 **/
package frc.team852.commands;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystems.LimitTesterSub;

public class LimitTester extends Command {
	private final LimitTesterSub limitTesterSub = Robot.limitTesterSub;
	
	//TODO Implement speed
	public LimitTester(int chan) {
		requires(Robot.limitTesterSub);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
	
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
	@Override
	protected void execute() {
		System.out.println(limitTesterSub.getDepressed());
	}
	
}
