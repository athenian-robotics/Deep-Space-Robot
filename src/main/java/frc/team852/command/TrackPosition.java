package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.Robot;
import frc.team852.lib.path.utilities.Pose2D;
import frc.team852.lib.utils.PositionTracking;
import frc.team852.subsystem.Drivetrain;

import static frc.team852.OI.stick1;
import static frc.team852.OI.stick2;

public class TrackPosition extends Command {

    private Drivetrain dt = Robot.drivetrain;
    private PositionTracking tracker;
    private Thread thread;

    public TrackPosition() {
        tracker = new PositionTracking();
        thread = new Thread(tracker);
        thread.run();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        thread.interrupt();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void execute() {
        // TODO @Jackson do something with pose data
        Pose2D pose = tracker.currPose.get();
        SmartDashboard.putNumberArray("Position Tracking Pose", new double[] {
                pose.getTranslation().getX(),
                pose.getTranslation().getY(),
                pose.getRotation().getAngle()});
    }
}
