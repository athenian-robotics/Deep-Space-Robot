package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.lib.utils.Shuffle;

public class DriveTimed extends Command {
    private double velocity;
    private double seconds;

    private boolean midActive;
    private double midPortion;
    private double midDistance;

    private static final Shuffle sVelocity = new Shuffle(DriveTimed.class, "velocity", 0);
    private static final Shuffle sSeconds = new Shuffle(DriveTimed.class, "seconds", 0);
    private static final Shuffle sMidPortion = new Shuffle(DriveTimed.class, "midPortion", 0);
    private static final Shuffle sMidDistance = new Shuffle(DriveTimed.class, "midDistance", 0);

    private long startTime;

    @Override
    protected void initialize() {
        velocity = sVelocity.get();
        seconds = sSeconds.get();
        midPortion = sMidPortion.get();
        midActive = false;
        startTime = System.currentTimeMillis();
        System.out.println("Started DriveTimed.");
    }

    @Override
    protected void end() {
        sMidDistance.set((Robot.drivetrain.getDistance() - midDistance) / (midPortion * seconds));
        System.out.println("Ended DriveTimed.");
    }

    @Override
    protected void execute() {
        if (!midActive && (System.currentTimeMillis() - startTime) / 1000d >= seconds * (1 - midPortion)) {
            midActive = true;
            midDistance = Robot.drivetrain.getDistance();
        }
        Robot.drivetrain.drive(velocity, velocity);
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
