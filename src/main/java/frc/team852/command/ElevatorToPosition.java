package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.utils.PIDControl;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorToPosition extends Command {

    private double targetDistance, currentDistance, speed;
    boolean raising;
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
        if (this.currentDistance >= targetDistance - RobotMap.elevatorDistanceError
                && this.currentDistance <= targetDistance + RobotMap.elevatorDistanceError)
        {
            System.out.println(this.getClass() + "is finished");
            return true;
        }
        return false;
    }

    @Override
    protected void end() {
        System.out.println(this.getClass() + "was ended");
    }

    @Override
    protected void interrupted() {
        System.out.println(this.getClass() + "should never be interrupted, ending()");
        end();
    }

    //TODO Tune PID Control
    @Override
    protected void execute()
    { // Called repeatedly when this command is scheduled to run
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
