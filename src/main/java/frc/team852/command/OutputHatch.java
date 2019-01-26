package frc.team852.command;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.HatchSubsystem;

public class OutputHatch extends Command {

    private HatchSubsystem hatchSubsystem = RobotMap.hatchSubsystem;
    private double time;

    public OutputHatch(){
        super();
        requires(Robot.hatchSubsystem);
        time = Timer.getFPGATimestamp();
    }

    @Override
    protected boolean isFinished() {
        return time + 0.5 <= Timer.getFPGATimestamp();
    }

    @Override
    protected void end() {
        hatchSubsystem.retractPneumatics();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void execute() {
        if(hatchSubsystem.getPneumaticState() != DoubleSolenoid.Value.kForward){
            hatchSubsystem.extendPneumatics();
            time = Timer.getFPGATimestamp();
        }
    }
}
