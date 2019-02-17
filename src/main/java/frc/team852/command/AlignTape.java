package frc.team852.command;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team852.DeepSpaceRobot.CameraPose;
import frc.team852.Robot;
import frc.team852.lib.callbacks.CallbackDataContainer;
import frc.team852.lib.callbacks.CameraPoseListener;
import frc.team852.lib.utils.Shuffle;

public class AlignTape extends CommandGroup {
    private CallbackDataContainer<CameraPose> cameraPose = new CallbackDataContainer<>();
    public static final Shuffle sCameraPose = new Shuffle(AlignTape.class, "cameraPose", "");

    public AlignTape() {
        Robot.dataServer.registerCallback(new CameraPoseListener() {
            @Override
            public void onNewData(CameraPose data) {
                cameraPose.update(data);
            }
        });
    }

    @Override
    protected void initialize() {
        CameraPose camera = cameraPose.getData();
        sCameraPose.set(camera.toString());

        // +X is right, +Y is up, +Z is forward
        double x = camera.getTranslation().getX();
        double y = camera.getTranslation().getZ();

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
