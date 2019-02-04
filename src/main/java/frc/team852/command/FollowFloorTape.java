package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.DeepSpaceRobot.GaffeTape;
import frc.team852.Robot;
import frc.team852.lib.callbacks.GaffeListener;
import frc.team852.subsystem.Drivetrain;


// TODO implement
public class FollowFloorTape extends Command implements GaffeListener {
  private boolean freshData = false;
  private int cyclesSinceFresh = 0;
  private double fwdPower = 0.25;
  private GaffeTape tape;
  private Drivetrain dt;

  public FollowFloorTape(){
    requires(Robot.drivetrain);
    // Register ourselves with the dataServer
    Robot.dataServer.registerCallback(this);
    dt = Robot.drivetrain;
  }

  @Override
  protected void end() {
    dt.stop();
  }

  @Override
  protected void execute() {
    if(freshData){
      freshData = false;
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  public void onNewData(GaffeTape data) {
    this.tape = data;
    this.freshData = true;
    this.cyclesSinceFresh = 0;
  }
}
