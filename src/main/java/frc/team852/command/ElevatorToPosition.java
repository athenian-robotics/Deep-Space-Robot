package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorToPosition extends Command {

    private double targetDistance, currentDistance, speed;
    private boolean raising;
    private final ElevatorSubsystem elevator = Robot.elevatorSubsystem;

    public ElevatorToPosition(double target, double speed) {
        requires(Robot.elevatorSubsystem);
        this.targetDistance = target;
        //this.currentDistance
        raising = targetDistance > currentDistance;
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }

    @Override
    protected void execute() {

    }
}
