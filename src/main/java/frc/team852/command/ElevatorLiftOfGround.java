package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorLiftOfGround extends Command {

    private static boolean wasInterrupted = false;
    private final ElevatorSubsystem elevator = Robot.elevatorSubsystem;
    private double targetDistance, move;
    private PIDController pid = new PIDController(0, 0, 0, RobotMap.elevatorLidar, RobotMap.wristMotor);


    //TODO Implement ElevatorLiftOfGround
    public ElevatorLiftOfGround() {
        requires(Robot.elevatorSubsystem);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        System.out.println(this.getClass() + "was ended");
        pid.setEnabled(false);
    }

    @Override
    protected void interrupted() {
        System.out.println(this.getClass() + " was interrupted");
        end();
    }

    @Override
    protected void execute() {
        //TODO Make this better
        if (elevator.getLidarDistance()[elevator.getLidarDistance().length] < 30 && RobotMap.wristMotor.getEncoderPosition() > 135 && elevator.getLidarDistance()[elevator.getLidarDistance().length] > 10) {
            pid.enable();
        } else {
            pid.disable();
        }
    }
}
