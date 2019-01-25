/**
 * A simple-as-possible subsystem tester for the SPARKMAX/NEO motor controller/motor dynamic duo.
 * @author Ezra Newman
 * @version 2019-01-19
 * @see frc.team852.command.NEOTester
 **/
package frc.team852.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.command.LimitTester;

public class LimitTesterSub extends Subsystem {
	private DigitalInput limit;
	
	public LimitTesterSub(int chan){
		super();
		limit =  new DigitalInput(chan);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new LimitTester(7));
	}
	
	public boolean getDepressed(){
		return limit.get();
	}
	
}
