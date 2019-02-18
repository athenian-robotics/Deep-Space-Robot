package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.utils.Shuffle;

public class DriveLogging extends Command {
    private static final Shuffle sGyroAngle = new Shuffle(DriveLogging.class, "GyroAngle", 0);
    private static final Shuffle sGyroFusedHeading = new Shuffle(DriveLogging.class, "GyroFusedHeading", 0);

    private static final Shuffle sGrayhillLeftRate = new Shuffle(DriveLogging.class, "Grayhill Left (Rate)", 0);
    private static final Shuffle sGrayhillLeftDistance = new Shuffle(DriveLogging.class, "Grayhill Left (Distance)", 0);
    private static final Shuffle sGrayhillRightRate = new Shuffle(DriveLogging.class, "Grayhill Right (Rate)", 0);
    private static final Shuffle sGrayhillRightDistance = new Shuffle(DriveLogging.class, "Grayhill Right (Distance)", 0);

    private static final Shuffle sLeftNeos = new Shuffle(DriveLogging.class, "LeftNeos", 0);
    private static final Shuffle sRightNeos = new Shuffle(DriveLogging.class, "RightNeos", 0);

    @Override
    protected void initialize() {
        System.out.println("DriveLogging started");
    }

    @Override
    protected void execute() {
        sGyroAngle.set(Robot.gyro.getAngle());
        sGyroFusedHeading.set(Robot.gyro.getFusedHeading());

        sGrayhillLeftRate.set(RobotMap.leftGrayhill.getRate());
        sGrayhillLeftDistance.set(RobotMap.leftGrayhill.getDistance());
        sGrayhillRightRate.set(RobotMap.rightGrayhill.getRate());
        sGrayhillRightDistance.set(RobotMap.rightGrayhill.getDistance());

        sLeftNeos.set(RobotMap.leftDrive.pidGet());
        sRightNeos.set(RobotMap.rightDrive.pidGet());


    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
