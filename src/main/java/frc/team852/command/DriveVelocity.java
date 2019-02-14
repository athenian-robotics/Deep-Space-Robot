package frc.team852.command;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team852.Robot;
import frc.team852.RobotMap;

public class DriveVelocity extends Command {
    private PIDController leftControl;
    private PIDController rightControl;
    private PIDSourceType leftSourceType;
    private PIDSourceType rightSourceType;

    private static ShuffleboardTab tab = Shuffleboard.getTab("Drive");
    private static NetworkTableEntry kpEntry = tab.add("DriveDistanceVelocity Kp", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private static NetworkTableEntry kiEntry = tab.add("DriveDistanceVelocity Ki", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private static NetworkTableEntry kdEntry = tab.add("DriveDistanceVelocity Kd", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();
    private static NetworkTableEntry kfEntry = tab.add("DriveDistanceVelocity Kf", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();

    private static NetworkTableEntry leftErrorEntry = tab.add("DriveDistanceVelocity leftError", 0)
            .getEntry();
    private static NetworkTableEntry rightErrorEntry = tab.add("DriveDistanceVelocity rightError", 0)
            .getEntry();
    private static NetworkTableEntry leftSetpointEntry = tab.add("DriveDistanceVelocity leftSetpoint", 0)
            .getEntry();
    private static NetworkTableEntry rightSetpointEntry = tab.add("DriveDistanceVelocity rightSetpoint", 0)
            .getEntry();
    private static NetworkTableEntry leftEncoderEntry = tab.add("DriveDistanceVelocity leftEncoder", 0)
            .getEntry();
    private static NetworkTableEntry rightEncoderEntry = tab.add("DriveDistanceVelocity rightEncoder", 0)
            .getEntry();
    private static NetworkTableEntry driveVelocityEnabled = tab.add("DriveDistanceVelocity enabled", false)
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
        System.out.println("DriveVelocity started.");
        driveVelocityEnabled.setBoolean(true);
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
        leftSetpointEntry.setNumber(leftControl.getSetpoint());
        rightSetpointEntry.setNumber(rightControl.getSetpoint());
        leftEncoderEntry.setNumber(RobotMap.leftEncoder.getRate());
        rightEncoderEntry.setNumber(RobotMap.rightEncoder.getRate());
        driveVelocityEnabled.setBoolean(true);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        driveVelocityEnabled.setBoolean(false);
        setSetpoints(0, 0);
        RobotMap.leftEncoder.setPIDSourceType(leftSourceType);
        RobotMap.leftEncoder.setPIDSourceType(rightSourceType);

        leftControl.reset();
        rightControl.reset();
        Robot.drivetrain.stop();
        System.out.println("DriveVelocity ended.");

    }

    @Override
    protected void interrupted() {
        driveVelocityEnabled.setBoolean(false);
        System.out.println("DriveVelocity interrupted.");
        end();
    }


    public void setSetpoints(double leftSetpoint, double rightSetpoint) {
        leftControl.setSetpoint(leftSetpoint);
        rightControl.setSetpoint(rightSetpoint);
    }

    public double getAbsError() {
        return Math.max(
                Math.abs(leftControl.getError()),
                Math.abs(rightControl.getError()));
    }
}
