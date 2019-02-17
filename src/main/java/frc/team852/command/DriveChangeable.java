package frc.team852.command;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.utils.Shuffle;
import frc.team852.subsystem.Drivetrain;

import static frc.team852.OI.xbox;

public class DriveChangeable extends Command {

  private Drivetrain dt = Robot.drivetrain;
  private double oldRate, currentRate, maxSlowRate, maxFastRate;
  private DifferentialDrive drive = new DifferentialDrive(RobotMap.leftDrive, RobotMap.rightDrive);
  private boolean squareInputs;

  private double lastTime;
  private double xSpeed;
  private double zRotation;

  private static final Shuffle sMaxAcceleration = new Shuffle(DriveChangeable.class, "maxAcceleration", 1000);
  private static final Shuffle sMaxDeceleration = new Shuffle(DriveChangeable.class, "maxDeceleration", 1000);

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

    Shuffle.put(this, "Gyro Angle", Robot.gyro.getAngle());
    Shuffle.put(this, "Gyro Fused Heading", Robot.gyro.getFusedHeading());
    Shuffle.put(this, "Grayhill Encoder Left (Rate)", RobotMap.leftGrayhill.getRate());
    Shuffle.put(this, "Grayhill Encoder Left (Distance)", RobotMap.leftGrayhill.getDistance());
    Shuffle.put(this, "Grayhill Encoder Right (Rate)", RobotMap.rightGrayhill.getRate());
    Shuffle.put(this, "Grayhill Encoder Right (Distance)", RobotMap.rightGrayhill.getDistance());

    Shuffle.put(this, "Left Neos", RobotMap.leftDrive.pidGet());
    Shuffle.put(this, "Right Neos", RobotMap.rightDrive.pidGet());


    // TODO replace old joystick tank/arcade with xbox joystick inputs
    Shuffle.put(this, "currentDriveMode", RobotMap.currentDriveMode.toString());
    if (RobotMap.currentDriveMode == Drivetrain.DriveMode.Tank) {
      drive.tankDrive(xbox.getTriggerAxis(GenericHID.Hand.kLeft), xbox.getTriggerAxis(GenericHID.Hand.kRight));
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.ArcadeJoy) {
      drive.arcadeDrive(xbox.getY(GenericHID.Hand.kLeft), xbox.getX(GenericHID.Hand.kLeft));
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.ArcadePad) {
      double multiplyBy = 0.6;
      if (xbox.getTriggerAxis(GenericHID.Hand.kRight) > 0.6)
        multiplyBy = xbox.getTriggerAxis(GenericHID.Hand.kRight);
      arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft) * multiplyBy, -xbox.getY(GenericHID.Hand.kLeft) * multiplyBy, true);
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.GTA) {
      double speed = -xbox.getTriggerAxis(GenericHID.Hand.kLeft) + xbox.getTriggerAxis(GenericHID.Hand.kRight);
      arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft), speed);

      double currentDecreasingRate = getDecreasing();
      if(dt.getGearing() == RobotMap.SLOW){
        if(currentDecreasingRate > maxSlowRate){
          maxSlowRate = currentDecreasingRate;
          SmartDashboard.putNumber("Max Decreasing Rate (slow speed)", maxSlowRate);
        }
      }
      if(dt.getGearing() == RobotMap.FAST){
        if(currentDecreasingRate > maxFastRate){
          maxFastRate = currentDecreasingRate;
          SmartDashboard.putNumber("Max Decreasing Rate (slow speed)", maxFastRate);
        }
      }
      if(speed > 0){
        xbox.setRumble(GenericHID.RumbleType.kRightRumble, getDecreasing());
      }
      else{
        xbox.setRumble(GenericHID.RumbleType.kLeftRumble, getDecreasing());
      }
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
    double maxAcceleration = sMaxAcceleration.get();
    double maxDeceleration = Math.max(maxAcceleration, sMaxDeceleration.get());

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
