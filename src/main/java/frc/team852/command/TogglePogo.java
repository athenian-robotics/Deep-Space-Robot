package frc.team852.command;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.ClimberSubsystem;

public class TogglePogo extends Command {

    private double startTime = 0;
    private boolean shouldRun;
    private ClimberSubsystem climber;

    public TogglePogo(){
        requires(Robot.climberSubsystem);
        climber = Robot.climberSubsystem;
        shouldRun = false;
    }

    @Override
    protected void initialize(){
        startTime = Timer.getFPGATimestamp();
        shouldRun = OI.xboxBack.get();
    }

    @Override
    protected boolean isFinished() {
        return (Timer.getFPGATimestamp() >= (startTime + 0.9) || !shouldRun);
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
        if(climber.getPogoState() != DoubleSolenoid.Value.kForward && shouldRun){
            climber.extendPogo();
        }
    }
}
