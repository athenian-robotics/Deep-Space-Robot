package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.Drivetrain;

public class DriveAngle extends Command {

    private double targetDist, inches;
    private Drivetrain dt = Robot.drivetrain;
    private PIDController leftPID = new PIDController(0.01,0,0,Robot.gyro, RobotMap.leftDrive);
    private PIDController rightPID = new PIDController(0.01,0,0, Robot.gyro,RobotMap.rightDrive);

    //1 revolution = 18.84 inches
    //1 revolution = 6.601 high gear, 15 low gear

    public DriveAngle(double angle){
        requires(dt);
        this.inches = angle;
    }

    @Override
    protected void initialize(){
        leftPID.setContinuous();
    }

    @Override
    protected boolean isFinished() {
        return leftPID.onTarget() && rightPID.onTarget();
    }

    @Override
    protected void end() {
        leftPID.setEnabled(false);
        rightPID.setEnabled(false);
        dt.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void execute() {
        if(!leftPID.isEnabled())
            leftPID.setEnabled(true);
        if(!rightPID.isEnabled())
            rightPID.setEnabled(true);

        System.out.println("leftPID error = " + leftPID.getError());
        System.out.println("rightPID error = " + rightPID.getError());
        //System.out.println("RobotMap.leftDrive.pidGet() = " + RobotMap.leftDrive.pidGet());
        //System.out.println("RobotMap.rightDrive.pidGet() = " + RobotMap.rightDrive.pidGet());
        //System.out.println("targetDist = " + targetDist);
    }
}
