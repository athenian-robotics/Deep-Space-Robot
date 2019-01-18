package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;

public class SampleCommand extends Command {

    public SampleCommand() {
        requires(Robot.sampleSubsystem);
    }

    /**
     * <p>
     * Called when the command is started
     * Make sure to (re-)initialize anything you need here
     * </p>
     */
    @Override
    protected void initialize() {

    }

    /**
     * <p>
     * If true is returned the command will be removed from the {@link edu.wpi.first.wpilibj.command.Scheduler}
     * And {@link #end()} will be called
     * </p>
     * <p>
     * Returning false will cause the command to never exit
     * Returning true will call execute once and exit
     * use {@link edu.wpi.first.wpilibj.command.InstantCommand} instead
     * </p>
     *
     * @return If the command is finished or not
     * @see Command#isTimedOut() isTimedOut()
     */

    @Override
    protected boolean isFinished() {
        return false;
    }

    /**
     * <p>
     * Called once when the command exits cleanly
     * when {@link #isFinished()} return true.
     * Use this to wrap up loose ends
     * </p>
     */
    @Override
    protected void end() {
    }

    /**
     * <p>
     * Called when the command ends because {@link #cancel()} was called
     * Or another command was called that required one or more
     * of the same subsystems as this command
     * </p><p>
     * Wrap up you loose ends here like shutting the motors that you were using down cleanly
     * </p><p>
     * Generally just call {@link #end()}
     * </p>
     */
    @Override
    protected void interrupted() {
    }

    /**
     * <p>
     * Called on repeat when the command is scheduled to run
     * No loops should exist in this method
     * </p>
     */
    @Override
    protected void execute() {
        // DO THE THING
    }
}
