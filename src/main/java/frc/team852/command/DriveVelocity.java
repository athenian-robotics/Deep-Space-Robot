package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.Drivetrain;

public class DriveVelocity extends Command {
    private PIDController leftControl;
    private PIDController rightControl;
    private double Kp;
    private double Ki;
    private double Kd;
    private double Kf;
    private double leftSetpoint;
    private double rightSetpoint;
    private PIDSourceType leftSourceType;
    private PIDSourceType rightSourceType;

    public DriveVelocity(double kp, double ki, double kd) {
        Kp = kp;
        Ki = ki;
        Kd = kd;
        leftSetpoint = 0;
        rightSetpoint = 0;
        leftControl = new PIDController(Kp, Ki, Kd, Kf, RobotMap.leftDrive, RobotMap.leftDrive);
        leftControl = new PIDController(Kp, Ki, Kd, Kf, RobotMap.rightDrive, RobotMap.rightDrive);
        requires(Robot.drivetrain);
    }


    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void initialize() {
        leftSourceType = RobotMap.leftDrive.getPIDSourceType();
        rightSourceType = RobotMap.rightDrive.getPIDSourceType();
        RobotMap.leftDrive.setPIDSourceType(PIDSourceType.kRate);
        RobotMap.rightDrive.setPIDSourceType(PIDSourceType.kRate);

        leftControl.enable();
        rightControl.enable();
        setSetpoints(0, 0);
        System.out.println("DriveVelocity started.");
    }

    @Override
    protected void execute() {
        // Nothing to do here since PIDController handles it all
    }

    @Override
    protected void end() {
        RobotMap.leftDrive.setPIDSourceType(leftSourceType);
        RobotMap.rightDrive.setPIDSourceType(rightSourceType);

        leftControl.reset();
        rightControl.reset();
        System.out.println("DriveVelocity ended.");

    }

    @Override
    protected void interrupted() {
        System.out.println("DriveVelocity interrupted.");
        end();
    }


    public void setSetpoints(double leftSetpoint, double rightSetpoint) {
        this.leftSetpoint = leftSetpoint;
        this.rightSetpoint = rightSetpoint;
        leftControl.setSetpoint(leftSetpoint);
        rightControl.setSetpoint(rightSetpoint);
    }

    public void setKp(double kp) {
        Kp = kp;
        leftControl.setP(kp);
        rightControl.setP(kp);
    }

    public void setKi(double ki) {
        Ki = ki;
        leftControl.setP(ki);
        rightControl.setP(ki);
    }

    public void setKd(double kd) {
        Kd = kd;
        leftControl.setP(kd);
        rightControl.setP(kd);
    }

    public void writeDashboard() {
        SmartDashboard.putNumber("DriveVelocity Kp", Kp);
        SmartDashboard.putNumber("DriveVelocity Ki", Ki);
        SmartDashboard.putNumber("DriveVelocity Kd", Kd);
        SmartDashboard.putNumber("DriveVelocity Left Setpoint", leftSetpoint);
        SmartDashboard.putNumber("DriveVelocity Right Setpoint", rightSetpoint);
    }
}
