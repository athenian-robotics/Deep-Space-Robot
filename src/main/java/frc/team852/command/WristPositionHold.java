package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.lib.utils.PIDControl;
import frc.team852.subsystem.WristSubsystem;

public class WristPositionHold extends Command
{
    private static boolean wasInterrupted = false;
    private final WristSubsystem wrist = Robot.wristSubsystem;
    private PIDControl pid = new PIDControl(0,0,0);
    private double move = 0, targetDistance;

    public WristPositionHold() {
        requires(Robot.elevatorSubsystem);
        this.targetDistance = wrist.getEncoderPos();
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end() { }

    @Override
    protected void interrupted()
    {
        wasInterrupted = true;
        end();
    }

    @Override
    protected void execute()
    {
        if(wasInterrupted) { wasInterrupted = false; }
        move = pid.getPID(this.targetDistance, wrist.getEncoderPos());
        wrist.setSpeed(move);
    }

}
