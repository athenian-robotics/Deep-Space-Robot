package frc.team852.command;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team852.Robot;
import frc.team852.subsystem.Drivetrain;

import static frc.team852.OI.xbox;

//import static frc.team852.OI.stick1;
//import static frc.team852.OI.stick2;

public class DriveTank extends Command {

  private Drivetrain dt = Robot.drivetrain;
  private boolean squareInputs;
  private double lastTime;
  private double leftSpeed;
  private double rightSpeed;

  private static ShuffleboardTab tab = Shuffleboard.getTab("Drive");
  private static NetworkTableEntry maxAccelerationEntry = tab.add("DriveTank maxAcceleration", 0)
          .getEntry();

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
    double currTime = System.currentTimeMillis();
    double dt = currTime - lastTime;
    lastTime = currTime;

    double leftTargetSpeed = xbox.getTriggerAxis(GenericHID.Hand.kLeft);
    double rightTargetSpeed = xbox.getTriggerAxis(GenericHID.Hand.kRight);
    double maxAcceleration = maxAccelerationEntry.getDouble(0);

    leftSpeed += sign(leftTargetSpeed - leftSpeed) *  Math.min(Math.abs(leftTargetSpeed - leftSpeed), Math.abs(maxAcceleration * dt));
    rightSpeed += sign(rightTargetSpeed - rightSpeed) *  Math.min(Math.abs(rightTargetSpeed - rightSpeed), Math.abs(maxAcceleration * dt));

    if (squareInputs) {
      leftSpeed = sign(leftSpeed) * (leftSpeed * leftSpeed);
      rightSpeed = sign(rightSpeed) * (rightSpeed * rightSpeed);
    }
    Robot.drivetrain.drive(leftSpeed, rightSpeed);
  }



  private double deadband(double value) {
    return Math.abs(value) > 0.1 ? value : 0;
  }

  private double sign(double num) {
    return num < 0 ? -1 : 1;
  }
}
