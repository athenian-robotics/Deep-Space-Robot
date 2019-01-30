package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.WristSubsystem;

//TODO encoders if necessary

public class WristTo0 extends Command{

    private final WristSubsystem wrist = Robot.wristSubsystem;

    public WristTo0() {
        super();
        requires(Robot.wristSubsystem);
    }

    @Override
    protected boolean isFinished(){ return RobotMap.wristLowerLimit.get(); }

    @Override
    protected void end(){
        wrist.stop();
        if (RobotMap.wristLowerLimit.get()) System.out.println("Wrist at 0 degrees");
    }

    @Override
    protected void execute(){
        if (!RobotMap.wristLowerLimit.get()) wrist.setSpeed(-.5);
        else end();
    }


}

