package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.ElevatorSubsystem;
import frc.team852.lib.utils.PIDControl;

public class ElevatorPositionHold extends Command
{
    private double targetDistance, move;
    private static boolean wasInterrupted = false;
    private final ElevatorSubsystem elevator = Robot.elevatorSubsystem;
    //private PIDControl pid = new PIDControl(0,0,0);

    //TODO Implement elevatorDistanceSensor
    public  ElevatorPositionHold() {
        requires(Robot.elevatorSubsystem);
        //this.targetDistance = RobotMap.elevatorDistanceSensor.get();
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {

    }

    @Override
    protected void interrupted()
    {
        wasInterrupted = true;
        end();
    }

    @Override
    protected void execute()
    {
        if(wasInterrupted) {
            //targetDistance = RobotMap.elevatorDistanceSensor.get();
            wasInterrupted = false;
        }


        //move = pid.getPID(this.targetDistance, RobotMap.elevatorDistanceSensor.get());
        elevator.setSpeed(move);
    }

}
