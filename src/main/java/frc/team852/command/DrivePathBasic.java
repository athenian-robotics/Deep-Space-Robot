package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.lib.path.utilities.Pose2D;
import frc.team852.subsystem.Drivetrain;
import frc.team852.lib.path.utilities.Trajectory;

import java.util.List;

import static frc.team852.OI.stick1;
import static frc.team852.OI.stick2;

/**
 * A naive implementation of driving along a sampled path.
 * @author Vincent Pisani
 */
public class DrivePathBasic extends Command {

    private Drivetrain dt = Robot.drivetrain;
    private List<Pose2D> points;
    private Pose2D prev;
    private int index;

    public DrivePathBasic(List<Pose2D> points) {
        this.points = points;
        prev = null;
        index = 0;
        requires(Robot.drivetrain);
    }

    @Override
    protected boolean isFinished() {
        return false;
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
        // TODO write this
    }
}
