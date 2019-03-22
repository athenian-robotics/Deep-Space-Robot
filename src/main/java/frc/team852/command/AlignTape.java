package frc.team852.command;

import com.google.protobuf.Empty;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team852.DeepSpaceRobot.CameraPose;
import frc.team852.Robot;
import frc.team852.lib.callbacks.CameraPoseListener;
import frc.team852.lib.path.utilities.Pose2D;
import frc.team852.lib.path.utilities.Translation2D;
import frc.team852.lib.utils.PositionTracking;
import frc.team852.lib.utils.Shuffle;
import frc.team852.lib.utils.datatypes.CallbackDataContainer;
import frc.team852.lib.utils.datatypes.InterpolatingDouble;
import io.grpc.stub.StreamObserver;

public class AlignTape extends CommandGroup {
    private CallbackDataContainer<CameraPose> cameraPose = new CallbackDataContainer<>();
    public static final Shuffle sCameraPose = new Shuffle(AlignTape.class, "cameraPose", "");

    public AlignTape() {
        Robot.dataServer.registerCallback(new CameraPoseListener() {
            @Override
            public void onNewData(CameraPose data, StreamObserver<Empty> respObsv) {
                cameraPose.update(data);
            }
        });
    }

    @Override
    protected void initialize() {
        CameraPose camera = cameraPose.getData();
        sCameraPose.set(camera.toString());

        // +X is right, +Y is up, +Z is forward
//        double x = camera.getTranslation().getX();
//        double y = camera.getTranslation().getZ();
        Translation2D compensation = feedforward();
        double x = camera.getTranslation().getX() + compensation.getX();
        double y = camera.getTranslation().getZ() + compensation.getY();

        // TODO This may be completely wrong - test to make sure it's in the right quadrant
        double arcCurvature = 2 * x / (x * x + y * y);
        double arcAngle = Math.copySign(Math.acos(1 - x * arcCurvature), x);
        double arcDistance = arcAngle / arcCurvature;

        double offsetAngle = Math.atan2(y, x);
        double turnAngle = offsetAngle - arcAngle;

        addSequential(new DriveAngle(turnAngle));
        addSequential(new DriveArcVelocity(arcDistance, arcCurvature, 0.1));
    }

    private Translation2D feedforward(){
        Pose2D frameCapturePose = Robot.positions.get(new InterpolatingDouble(cameraPose.timestamp));
        Pose2D curVal = PositionTracking.getPose();
        return new Translation2D(curVal.getTranslation().compose(frameCapturePose.getTranslation().inverse())); // Get move since frame capture
    }
}
