package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.Drivetrain;

public class DriveAngle extends Command {

    private double targetAngle, error;
    private Drivetrain dt = Robot.drivetrain;
    private PIDController pid = new PIDController(0.01,0,0,Robot.gyro, RobotMap.leftDrive);

    public DriveAngle(double angle){
        requires(dt);
        this.targetAngle = angle;
    }

    @Override
    protected void initialize(){
        pid.setContinuous(false);
        pid.setSetpoint(Robot.gyro.getAngle() + targetAngle);
        pid.setAbsoluteTolerance(2);
    }

    @Override
    protected boolean isFinished() {
        return pid.onTarget();
    }

    @Override
    protected void end() {
        dt.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void execute() {
        error = pid.getError();
        System.out.println("pid error = " + error);
        if(error < 0){
            dt.drive(-error, error);
        }
        else{
            dt.drive(error, -error);
        }
    }
}
