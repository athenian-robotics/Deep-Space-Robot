package frc.team852.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.WristSubsystem;

public class WristBangBang extends Command {

    private WristSubsystem wrist;
    private boolean firstDown = true;
    private double startTime = 0;

    public static boolean isUp = true;

    public WristBangBang(){
        requires(Robot.wristSubsystem);
        wrist = Robot.wristSubsystem;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    protected void initialize(){
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        if(firstDown){
            if(startTime + 0.1 <= Timer.getFPGATimestamp())
                wrist.setSpeed(0.2);
            else{
                wrist.setSpeed(0);
                firstDown = false;
            }
        }
        else {
            if (!OI.fightStickLB.get() && !OI.fightStickShare.get()) {
                if (OI.POVDown.get()) {
                    wrist.setSpeed(-0.7);
                    isUp = true;
                } else if (OI.POVUp.get()) {
                    wrist.setSpeed(0.6);
                    isUp = false;
                } else {
                    wrist.setSpeed(isUp ? -0.2 : 0.2);
                }
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
