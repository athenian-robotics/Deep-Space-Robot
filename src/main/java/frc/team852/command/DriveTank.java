package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.Drivetrain;

import static frc.team852.OI.stick1;
import static frc.team852.OI.stick2;

public class DriveTank extends Command {

  private Drivetrain dt = Robot.drivetrain;
  private boolean squareInputs;

  public DriveTank() {
    this(false);
  }

  public DriveTank(boolean squareInputs) {
    this.squareInputs = squareInputs;
    requires(Robot.drivetrain);
  }

  /**
   *
   * @return false 'cuz stopping the drivetrain would be stupid
   */
  @Override
  protected boolean isFinished() {
    return false;
  }

  /**
   * If it is told to stop, stop gracefully
   */
  @Override
  protected void end() {
    dt.stop();
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void execute() {
    double leftSpeed = deadband(stick1.getY());
    double rightSpeed = deadband(stick2.getY());
    if (squareInputs) {
      leftSpeed = sign(leftSpeed) * (leftSpeed * leftSpeed);
      rightSpeed = sign(rightSpeed) * (rightSpeed * rightSpeed);
    }
    dt.drive(leftSpeed, rightSpeed);
  }



  private double deadband(double value) {
    return Math.abs(value) > 0.1 ? value : 0;
  }

  private double sign(double num) {
    return num < 0 ? -1 : 1;
  }
}
