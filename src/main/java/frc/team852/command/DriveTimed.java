package frc.team852.command;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team852.Robot;

public class DriveTimed extends Command {
    private double velocity;
    private double seconds;

    private boolean midActive;
    private double midPortion;
    private double midDistance;

    private static ShuffleboardTab tab = Shuffleboard.getTab("Drive");
    private static NetworkTableEntry velocityEntry = tab.add("DriveTimed velocity", 0)
            .getEntry();
    private static NetworkTableEntry secondsEntry = tab.add("DriveTimed seconds", 0)
            .getEntry();
    private static NetworkTableEntry midPortionEntry = tab.add("DriveTimed midPortion", 0)
            .getEntry();
    private static NetworkTableEntry midDistanceEntry = tab.add("DriveTimed midDistance", 0)
            .getEntry();

    private long startTime;

    @Override
    protected void initialize() {
        velocity = velocityEntry.getDouble(0);
        seconds = secondsEntry.getDouble(0);
        midPortion = midPortionEntry.getDouble(0);
        midActive = false;
        startTime = System.currentTimeMillis();
        System.out.println("Started DriveTimed.");
    }

    @Override
    protected void end() {
        midDistanceEntry.setNumber((Robot.drivetrain.getDistance() - midDistance) / (midPortion * seconds));
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
