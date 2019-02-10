package frc.team852.command;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AlignTape extends CommandGroup {
    @Override
    protected void initialize() {
        // TODO Get necessary coordinates from Donovan
        double x = 0;
        double y = 0;

        // TODO This may be completely wrong - test to make sure it's in the right quadrant
        double arcCurvature = 2 * x / (x * x + y * y);
        double arcAngle = Math.copySign(Math.acos(1 - x * arcCurvature), x);
        double arcDistance = arcAngle / arcCurvature;

        double offsetAngle = Math.atan2(y, x);
        double turnAngle = offsetAngle - arcAngle;

        addSequential(new DriveAngle(turnAngle));
        addSequential(new DriveArcVelocity(arcDistance, arcCurvature, 0.1));
    }
}
