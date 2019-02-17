package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.utils.Shuffle;
import frc.team852.lib.utils.SmoothSpeedOutput;

public class DriveVelocity extends Command {
    private PIDController leftControl;
    private PIDController rightControl;
    private PIDSourceType leftSourceType;
    private PIDSourceType rightSourceType;

    private static final Shuffle sKp = new Shuffle(DriveVelocity.class, "Kp", 0.01);

    private static final Shuffle sKi = new Shuffle(DriveVelocity.class, "Ki", 0);
    private static final Shuffle sKd = new Shuffle(DriveVelocity.class, "Kd", 0.1);
    private static final Shuffle sKf = new Shuffle(DriveVelocity.class, "Kf", 0.275);

    private static final Shuffle sLeftError = new Shuffle(DriveVelocity.class, "leftError", 0);
    private static final Shuffle sRightError = new Shuffle(DriveVelocity.class, "rightError", 0);
    private static final Shuffle sLeftSetpoint = new Shuffle(DriveVelocity.class, "leftSetpoint", 0);
    private static final Shuffle sRightSetpoint = new Shuffle(DriveVelocity.class, "rightSetpoint", 0);
    private static final Shuffle sLeftGrayhill = new Shuffle(DriveVelocity.class, "leftGrayhill", 0);
    private static final Shuffle sRightGrayhill = new Shuffle(DriveVelocity.class, "rightGrayhill", 0);
    private static final Shuffle sEnabled = new Shuffle(DriveVelocity.class, "enabled", false);

    public DriveVelocity(double kp, double ki, double kd, double kf) {
        sKp.set(kp);
        sKi.set(ki);
        sKd.set(kd);
        sKf.set(kf);
        prepare();
    }

    public DriveVelocity() {
        prepare();
    }

    private void prepare() {
        leftControl = new PIDController(
                sKp.get(),
                sKi.get(),
                sKd.get(),
                sKf.get(),
                RobotMap.leftGrayhill, new SmoothSpeedOutput(RobotMap.leftDrive, 1, true));
        rightControl = new PIDController(
                sKp.get(),
                sKi.get(),
                sKd.get(),
                sKf.get(),
                RobotMap.rightGrayhill, new SmoothSpeedOutput(RobotMap.rightDrive, 1, false));

        requires(Robot.drivetrain);
    }


    @Override
    protected void initialize() {
        leftSourceType = RobotMap.leftGrayhill.getPIDSourceType();
        rightSourceType = RobotMap.rightGrayhill.getPIDSourceType();
        RobotMap.leftGrayhill.setPIDSourceType(PIDSourceType.kRate);
        RobotMap.rightGrayhill.setPIDSourceType(PIDSourceType.kRate);

        leftControl.enable();
        rightControl.enable();
        System.out.println("DriveVelocity started.");
        sEnabled.set(true);
    }

    @Override
    protected void execute() {
        double kp = sKp.get();
        double ki = sKi.get();
        double kd = sKd.get();
        double kf = sKf.get();

        leftControl.setP(kp);
        leftControl.setI(ki);
        leftControl.setD(kd);
        leftControl.setF(kf);

        rightControl.setP(kp);
        rightControl.setI(ki);
        rightControl.setD(kd);
        rightControl.setF(kf);

        sLeftError.set(leftControl.getError());
        sRightError.set(rightControl.getError());
        sLeftSetpoint.set(leftControl.getSetpoint());
        sRightSetpoint.set(rightControl.getSetpoint());
        sLeftGrayhill.set(RobotMap.leftGrayhill.getRate());
        sRightGrayhill.set(RobotMap.rightGrayhill.getRate());
        sEnabled.set(true);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        sEnabled.set(false);
        setSetpoints(0, 0);
        RobotMap.leftGrayhill.setPIDSourceType(leftSourceType);
        RobotMap.rightGrayhill.setPIDSourceType(rightSourceType);

        leftControl.reset();
        rightControl.reset();
        Robot.drivetrain.stop();
        System.out.println("DriveVelocity ended.");

    }

    @Override
    protected void interrupted() {
        sEnabled.set(false);
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
