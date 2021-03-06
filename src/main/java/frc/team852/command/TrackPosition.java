package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.Robot;
import frc.team852.lib.utils.PositionTracking;
import frc.team852.subsystem.Drivetrain;

public class TrackPosition extends Command {

  private Drivetrain dt = Robot.drivetrain;
//    private PositionTracking tracker = new PositionTracking();
//    private Thread thread;

  public TrackPosition() {
//        thread = new Thread(tracker);
//        thread.start();
    if (!PositionTracking.isStarted())
      PositionTracking.getInstance().start();
    System.out.println("Starting position tracking...");
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    PositionTracking.stop();
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void execute() {
    // TODO @Jackson do something with pose data
    SmartDashboard.putNumber("Position Tracking X", PositionTracking.getCurrX());
    SmartDashboard.putNumber("Position Tracking Y", PositionTracking.getCurrY());
    SmartDashboard.putNumber("Position Tracking Angle", PositionTracking.getCurrAngle());
    //*/
  }
}
