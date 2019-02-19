package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.WristSubsystem;

public class WristBangBang extends Command {

    private WristSubsystem wrist;

    public WristBangBang(){
        requires(Robot.wristSubsystem);
        wrist = Robot.wristSubsystem;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {
        if(!OI.fightStickLB.get()){
            if(OI.POVDown.get() && wrist.canMoveUp()){
                wrist.setSpeed(0.3);
            }
            else if(OI.POVUp.get()){
                wrist.setSpeed(-0.3);
            }
        }
    }

    @Override
    protected void end() {
        wrist.stopMotors();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
