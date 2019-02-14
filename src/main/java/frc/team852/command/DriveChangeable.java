package frc.team852.command;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.Drivetrain;

import static frc.team852.OI.xbox;

public class DriveChangeable extends Command {

  private Drivetrain dt = Robot.drivetrain;
  private double oldRate, currentRate, maxRate;
  private DifferentialDrive drive = new DifferentialDrive(RobotMap.leftDrive, RobotMap.rightDrive);
  private boolean squareInputs;

  private double lastTime;
  private double xSpeed;
  private double zRotation;

  private static ShuffleboardTab tab = Shuffleboard.getTab("Drive");
  private static NetworkTableEntry maxAccelerationEntry = tab.add("DriveChangeable maxAcceleration", 0)
          .getEntry();
  private static NetworkTableEntry maxDecelerationEntry = tab.add("DriveChangeable maxDeceleration", 1000)
          .getEntry();

  public DriveChangeable() {
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

    currentRate = dt.getRate();

    SmartDashboard.putNumber("Gyro Angle", Robot.gyro.getAngle());
    SmartDashboard.putNumber("Gyro Fused Heading", Robot.gyro.getFusedHeading());
    SmartDashboard.putNumber("Grayhill Encoder Left (get)", RobotMap.leftEncoder.get());
    SmartDashboard.putNumber("Grayhill Encoder Left (Distance)", RobotMap.leftEncoder.getDistance());
    SmartDashboard.putNumber("Grayhill Encoder Left (pid)", RobotMap.leftEncoder.pidGet());
    SmartDashboard.putNumber("Grayhill Encoder Right (get)", RobotMap.rightEncoder.get());
    SmartDashboard.putNumber("Grayhill Encoder Right (Distance)", RobotMap.rightEncoder.getDistance());
    SmartDashboard.putNumber("Grayhill Encoder Right (pid)", RobotMap.rightEncoder.pidGet());

    SmartDashboard.putNumber("Left Neos", RobotMap.leftEncoder.pidGet());
    SmartDashboard.putNumber("Right Neos", RobotMap.rightEncoder.pidGet());


    // TODO replace old joystick tank/arcade with xbox joystick inputs
    if (RobotMap.currentDriveMode == Drivetrain.DriveMode.Tank) {
      //drive.tankDrive(-stick2.getY(), stick1.getY(), true);
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.ArcadeJoy) {
      //arcadeDrive(-stick1.getX(), -stick1.getY(), true);
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.ArcadePad) {
      double multiplyBy = 0.6;
      if (xbox.getTriggerAxis(GenericHID.Hand.kRight) > 0.6)
        multiplyBy = xbox.getTriggerAxis(GenericHID.Hand.kRight);
      arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft) * multiplyBy, -xbox.getY(GenericHID.Hand.kLeft) * multiplyBy, true);
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.GTA) {
      double speed = -xbox.getTriggerAxis(GenericHID.Hand.kLeft) + xbox.getTriggerAxis(GenericHID.Hand.kRight);
      arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft), speed);

      double currentDecreasingRate = getDecreasing();
      if(currentDecreasingRate > maxRate) {
        maxRate = currentDecreasingRate;
        SmartDashboard.putNumber("Max Decreasing Rate", maxRate);
      }
//      if(speed > 0){
//        xbox.setRumble(GenericHID.RumbleType.kRightRumble, getDecreasing());
//      }
//      else{
//        xbox.setRumble(GenericHID.RumbleType.kLeftRumble, getDecreasing());
//      }
    }

    oldRate = dt.getRate();
  }

  // TODO migrate to a more sensible and general place
  public void arcadeDrive(double xSpeed, double zRotation, boolean squareInputs) {
    double currTime = System.currentTimeMillis();
    double deltaTime = (currTime - lastTime) / 1000d;
    lastTime = currTime;

    if (squareInputs) {
      xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
      zRotation = Math.copySign(zRotation * zRotation, zRotation);
    }

    double xSpeedError = xSpeed - this.xSpeed;
    double zRotationError = zRotation - this.zRotation;
    double maxAcceleration = maxAccelerationEntry.getDouble(0);
    double maxDeceleration = Math.max(maxAcceleration, maxDecelerationEntry.getDouble(0));

    this.xSpeed += Math.copySign(
            Math.max(
                    -Math.abs(maxDeceleration * deltaTime),
                    Math.min(
                            Math.abs(maxAcceleration * deltaTime),
                            Math.copySign(xSpeedError, this.xSpeed * xSpeedError)
                    )),
            xSpeedError);
    this.zRotation += Math.copySign(
            Math.max(
                    -Math.abs(maxDeceleration * deltaTime),
                    Math.min(
                            Math.abs(maxAcceleration * deltaTime),
                            Math.copySign(zRotationError, this.zRotation * zRotationError)
                    )),
            zRotationError);

    drive.arcadeDrive(this.xSpeed, this.zRotation, false);
  }

  public void arcadeDrive(double xSpeed, double zRotation) {
    arcadeDrive(xSpeed, zRotation, false);
  }

  private double getDecreasing(){
    double finalRate = oldRate - currentRate;
    if(finalRate > 0.05){
      //TODO: implement scaling from 0-1
      return finalRate;
    }
    return 0.0;
  }

  private double smooth(double input){
    //See: https://www.desmos.com/calculator/h0noo6swsh
    input = limit(input);

    if(input == 0){
      return 0.0;
    }
    if(input<=0.4){
      return Math.cbrt(input)*0.678604;
    }
    else if(input<=0.6){
      return 0.5;
    }
    else if(input<=0.85){
      return (double)(1/3)*Math.tan(input+0.4)-0.0191359;
    }
    else{
      return 1.0;
    }
  }

  protected double limit(double value) {
    if (value > 1.0) {
      return 1.0;
    }
    if (value <= 0) {
      return 0.0;
    }
    return value;
  }
}
