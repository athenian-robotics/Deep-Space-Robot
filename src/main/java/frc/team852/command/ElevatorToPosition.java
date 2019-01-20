package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.ElevatorSubsystem;
import frc.team852.utils.PIDControl;

public class ElevatorToPosition extends Command {

    private double targetDistance, currentDistance, speed;
    private boolean raising;
    private final ElevatorSubsystem elevator = Robot.elevatorSubsystem;
    private double distance = 0;
    private PIDControl pid = new PIDControl(0,0,0);

    //TODO Implement speed, elevatorDistanceSensor
    public ElevatorToPosition(double target, double speed) {
        requires(Robot.elevatorSubsystem);
        this.targetDistance = target;
        //this.currentDistance = RobotMap.elevatorDistanceSensor.get();
        raising = targetDistance > currentDistance;
    }

    @Override
    protected boolean isFinished() {
        //this.currentDistance = RobotMap.elevatorDistanceSensor.get();
        return this.currentDistance >= targetDistance-RobotMap.elevatorDistanceError && this.currentDistance <= targetDistance+RobotMap.elevatorDistanceError;
    }

    @Override
    protected void end() {
        System.out.println("Elevator Returning To Hold Position");
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void execute() {
        //this.currentDistance = RobotMap.elevatorDistanceSensor.get();
        double move = pid.getPID(this.targetDistance, this.currentDistance);
        if(move > 0 && RobotMap.elevatorUpperLimit.get()){
            System.out.println("Elevator Upper Limit Triggered!");
            end();
        }
        if(move < 0 && RobotMap.elevatorLowerLimit.get()){
            System.out.println("Elevator Lower Limit Triggered!");
            end();
        }
        elevator.setSpeed(move);
    }
}
