package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.WristSubsystem;

public class WristTo90 extends Command{
    
    private final WristSubsystem wrist = Robot.wristSubsystem;

    public WristTo90() {
        requires(Robot.wristSubsystem);
    }

    @Override
    protected boolean isFinished(){
        return RobotMap.wristUpperLimit.get();
    }
    
    @Override
    protected void end(){
        wrist.stopMotors();
        System.out.println("Wrist at 90 degrees");
    }

    @Override
    protected void execute(){
        if (!isFinished()) wrist.setSpeed(.5);
        else end();
    }
    
    
}
