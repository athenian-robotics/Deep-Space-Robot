package frc.team852.command;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.ClimberSubsystem;

public class TogglePogo extends Command {

    private boolean shouldRun, down;
    private ClimberSubsystem climber;

    public TogglePogo(){
        requires(Robot.climberSubsystem);
        climber = Robot.climberSubsystem;
        shouldRun = false;
        down = true;
    }

    @Override
    protected void initialize(){
        down = climber.getPogoState() == DoubleSolenoid.Value.kReverse;
        shouldRun = OI.xboxBack.get();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end(){
        if(climber.getPogoState() == DoubleSolenoid.Value.kForward)
            climber.retractPogo();
    }

    @Override
    protected void interrupted(){
        end();
    }

    @Override
    protected void execute(){
        if(shouldRun){
            if (down)
                climber.extendPogo();
            else
                climber.retractPogo();
        }
    }
}
