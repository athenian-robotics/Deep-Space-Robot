package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.DeepSpaceRobot.GaffeTape;
import frc.team852.DeepSpaceRobot.ReflTapePair;
import frc.team852.Robot;
import frc.team852.lib.callbacks.GaffeListener;
import frc.team852.lib.callbacks.HatchListener;
import frc.team852.lib.callbacks.ReflTapeListener;
import frc.team852.lib.callbacks.CallbackDataContainer;
import frc.team852.subsystem.Drivetrain;


// TODO implement
public class FollowFloorTape extends Command {
  private CallbackDataContainer<GaffeTape> gaffeTape = new CallbackDataContainer<>();
  private CallbackDataContainer<ReflTapePair> reflTapePair = new CallbackDataContainer<>();
  private double fwdPower = 0.25;
  private Drivetrain dt;

  public FollowFloorTape() {
    requires(Robot.drivetrain);
    // Register ourselves with the dataServer
    Robot.dataServer.registerCallback(new GaffeListener() {
      @Override
      public void onNewData(GaffeTape data) {
        gaffeTape.update(data);
      }
    });
    Robot.dataServer.registerCallback(new ReflTapeListener() {
      @Override
      public void onNewData(ReflTapePair data) {
        reflTapePair.update(data);
      }
    });
    dt = Robot.drivetrain;
  }

  @Override
  protected void end() {
    dt.stop();
  }

  @Override
  protected void execute() {
    // TODO implement
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}