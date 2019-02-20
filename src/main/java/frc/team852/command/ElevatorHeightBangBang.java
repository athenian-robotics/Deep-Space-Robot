package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.lib.utils.SerialLidar;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorHeightBangBang extends Command {
  private int targetHeight;
  private final ElevatorSubsystem elevator;
  private final SerialLidar lidar;
  private final int TOLERANCE = 2;

  public ElevatorHeightBangBang(int height) {
    requires(Robot.elevatorSubsystem);
    elevator = Robot.elevatorSubsystem;
    lidar = Robot.elevatorLidar;
    targetHeight = height;
  }

  @Override
  protected void execute() {
    int curHeight = lidar.getLidarDistance();
    int error = targetHeight - curHeight;
    double speed = 0.04; // Hold power
    if (error > 0 && Math.abs(error) >= TOLERANCE) {
      speed = 0.3;
    } else if (error < 0 && Math.abs(error) >= TOLERANCE) {
      speed = -0.2;
    }
//    if(speed > 0 && elevator.)



  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
