package frc.team852.command;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.Robot;
import frc.team852.RobotMap;

public class DriveVelocity extends Command {
    private PIDController leftControl;
    private PIDController rightControl;
    private double leftSetpoint;
    private double rightSetpoint;
    private PIDSourceType leftSourceType;
    private PIDSourceType rightSourceType;

    private ShuffleboardTab tab = Shuffleboard.getTab("Drive");
    private NetworkTableEntry kpEntry = tab.add("DriveDistanceVelocity Kp", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private NetworkTableEntry kiEntry = tab.add("DriveDistanceVelocity Ki", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private NetworkTableEntry kdEntry = tab.add("DriveDistanceVelocity Kd", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private NetworkTableEntry kfEntry = tab.add("DriveDistanceVelocity Kf", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();

    private NetworkTableEntry leftErrorEntry = tab.add("DriveDistanceVelocity leftError", 0)
            .getEntry();
    private NetworkTableEntry rightErrorEntry = tab.add("DriveDistanceVelocity rightError", 0)
            .getEntry();

    public DriveVelocity(double kp, double ki, double kd, double kf) {
        kpEntry.setNumber(kp);
        kiEntry.setNumber(ki);
        kdEntry.setNumber(kd);
        kfEntry.setNumber(kf);
        prepare();
    }

    public DriveVelocity() {
        prepare();
    }

    private void prepare() {
        leftSetpoint = 0;
        rightSetpoint = 0;

        leftControl = new PIDController(
                kpEntry.getDouble(0),
                kiEntry.getDouble(0),
                kdEntry.getDouble(0),
                kfEntry.getDouble(0),
                RobotMap.leftEncoder, RobotMap.leftDrive);
        rightControl = new PIDController(
                kpEntry.getDouble(0),
                kiEntry.getDouble(0),
                kdEntry.getDouble(0),
                kfEntry.getDouble(0),
                RobotMap.rightEncoder, RobotMap.rightDrive);

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
        double kp = kpEntry.getDouble(0);
        double ki = kiEntry.getDouble(0);
        double kd = kdEntry.getDouble(0);
        double kf = kfEntry.getDouble(0);

        leftControl.setP(kp);
        leftControl.setI(ki);
        leftControl.setD(kd);
        leftControl.setF(kf);

        rightControl.setP(kp);
        rightControl.setI(ki);
        rightControl.setD(kd);
        rightControl.setF(kf);

        leftErrorEntry.setNumber(leftControl.getError());
        rightErrorEntry.setNumber(rightControl.getError());
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

    public double getAbsError() {
        return Math.max(
                Math.abs(leftControl.getError()),
                Math.abs(rightControl.getError()));
    }
}
