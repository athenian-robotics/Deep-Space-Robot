package frc.team852.command;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.Drivetrain;
import frc.team852.lib.utils.PIDControl;

public class DriveDistance extends Command {
  private double distance;
  private final Drivetrain dt;
  private final AHRS navx;
  private PIDControl drive, turn;

  /**
   * Create a new DriveDistance command
   *
   * @param distance in centimeters
   */
  public DriveDistance(double distance) {
    requires(Robot.drivetrain);
    dt = Robot.drivetrain;
    this.distance = distance;
    this.navx = Robot.gyro;
    this.navx.reset();
    drive = new PIDControl(0, 0, 0);
    turn = new PIDControl(0, 0, 0);
  }

  @Override
  protected void execute() {


  }


  @Override
  protected boolean isFinished() {
    return false;
  }
}
