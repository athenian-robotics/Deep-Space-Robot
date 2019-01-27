package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorToPosition extends Command {

    private double targetDistance, currentDistance;
    private final ElevatorSubsystem elevator = Robot.elevatorSubsystem;
    private PIDController pid = new PIDController(0, 0, 0, RobotMap.lidar, RobotMap.elevatorMotors);

    //TODO Implement speed
    public ElevatorToPosition(double target) {
        requires(Robot.elevatorSubsystem);
        this.targetDistance = target;
        this.currentDistance = elevator.getLidarDistance()[0];
        pid.setContinuous(false);
        pid.setSetpoint(target);
        pid.setPercentTolerance(1);
    }

    @Override
    protected void initialize(){
        pid.reset();
    }

    @Override
    protected boolean isFinished() {
        return pid.onTarget();
    }

    @Override
    protected void end() {
        System.out.println(this.getClass() + "was ended");
        pid.setEnabled(false);
        //TODO return control to hold
    }

    @Override
    protected void interrupted() {
        System.out.println(this.getClass() + "should never be interrupted, ending()");
        end();
    }

    //TODO Tune PID Control
    @Override
    protected void execute()
    {
        if(!pid.isEnabled()) pid.setEnabled(true);
    }
}
