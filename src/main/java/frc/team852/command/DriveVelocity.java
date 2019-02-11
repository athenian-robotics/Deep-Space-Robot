package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.Robot;
import frc.team852.RobotMap;

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

    public DriveVelocity(double kp, double ki, double kd, double kf) {
        Kp = kp;
        Ki = ki;
        Kd = kd;
        Kf = kf;
        leftSetpoint = 0;
        rightSetpoint = 0;
        leftControl = new PIDController(Kp, Ki, Kd, Kf, RobotMap.leftEncoder, RobotMap.leftDrive);
        rightControl = new PIDController(Kp, Ki, Kd, Kf, RobotMap.rightEncoder, RobotMap.rightDrive);
        requires(Robot.drivetrain);
    }


    @Override
    protected void initialize() {
        leftSourceType = RobotMap.leftEncoder.getPIDSourceType();
        rightSourceType = RobotMap.rightEncoder.getPIDSourceType();
        RobotMap.leftEncoder.setPIDSourceType(PIDSourceType.kRate);
        RobotMap.rightEncoder.setPIDSourceType(PIDSourceType.kRate);

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
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        RobotMap.leftEncoder.setPIDSourceType(leftSourceType);
        RobotMap.leftEncoder.setPIDSourceType(rightSourceType);

        leftControl.reset();
        rightControl.reset();
        Robot.drivetrain.stop();
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
        leftControl.setI(ki);
        rightControl.setI(ki);
    }

    public void setKd(double kd) {
        Kd = kd;
        leftControl.setD(kd);
        rightControl.setD(kd);
    }

    public void setKf(double kf) {
        Kd = kf;
        leftControl.setF(kf);
        rightControl.setF(kf);
    }

    public void setAll(double kp, double ki, double kd, double kf) {
        setKp(kp);
        setKi(ki);
        setKd(kd);
        setKf(kf);
    }

    public double getAbsError() {
        return Math.max(
                Math.abs(leftControl.getError()),
                Math.abs(rightControl.getError()));
    }

    public void writeDashboard() {
        SmartDashboard.putNumber("DriveVelocity Kp", Kp);
        SmartDashboard.putNumber("DriveVelocity Ki", Ki);
        SmartDashboard.putNumber("DriveVelocity Kd", Kd);
        SmartDashboard.putNumber("DriveVelocity Kf", Kd);
        SmartDashboard.putNumber("DriveVelocity Left Setpoint", leftSetpoint);
        SmartDashboard.putNumber("DriveVelocity Right Setpoint", rightSetpoint);
    }
}
