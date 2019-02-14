package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTimedVelocity extends Command {
    private final DriveVelocity driveVelocity;
    private final double targetVelocity;
    private final double seconds;

    private long startTime;

    public DriveTimedVelocity(double targetVelocity, double seconds) {
        driveVelocity = new DriveVelocity();
        this.targetVelocity = targetVelocity;
        this.seconds = seconds;
    }

    @Override
    protected void initialize() {
        startTime = System.currentTimeMillis();
        driveVelocity.start();
        driveVelocity.setSetpoints(targetVelocity, targetVelocity);
    }

    @Override
    protected void end() {
        driveVelocity.cancel();
    }

    @Override
    protected void execute() {
        // Nothing to do here since DriveVelocity runs on its own
    }

    @Override
    protected boolean isFinished() {
        return (System.currentTimeMillis() - startTime) / 1000d >= seconds;
    }

    @Override
    protected void interrupted() {
        end();
    }
}
