package frc.team852.command;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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

  private static final Shuffle sMaxAccelFast = new Shuffle(DriveChangeable.class, "maxAccelFast", 1.5);
  private static final Shuffle sMaxDecelFast = new Shuffle(DriveChangeable.class, "maxDecelFast", 2);
  private static final Shuffle sMaxAccelSlow = new Shuffle(DriveChangeable.class, "maxAccelSlow", 1.5);
  private static final Shuffle sMaxDecelSlow = new Shuffle(DriveChangeable.class, "maxDecelSlow", 2);
  private static final Shuffle sRotationAccelScale = new Shuffle(DriveChangeable.class, "RotationAccelScale", 1);

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
      arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft), -xbox.getTriggerAxis(GenericHID.Hand.kLeft) + xbox.getTriggerAxis(GenericHID.Hand.kRight));
    }
  }

  // TODO migrate to a more sensible and general place
  public void arcadeDrive(double zRotation, double xSpeed, boolean squareInputs) {
    double currTime = System.currentTimeMillis();
    double deltaTime = (currTime - lastTime) / 1000d;
    lastTime = currTime;

    if (squareInputs) {
      this.xSpeed = Math.copySign(this.xSpeed * this.xSpeed, this.xSpeed);
      this.zRotation = Math.copySign(this.zRotation * this.zRotation, this.zRotation);
    }

    double xSpeedError = xSpeed - this.xSpeed;
    double zRotationError = zRotation - this.zRotation;

    double maxAcceleration, maxDeceleration;
    boolean fastGear = (Robot.drivetrain.getGearing() == RobotMap.FAST);
    maxAcceleration = Math.abs(fastGear ? sMaxAccelFast.get() : sMaxAccelSlow.get());
    maxDeceleration = Math.abs(fastGear ? sMaxDecelFast.get() : sMaxDecelSlow.get());
    double rotationAccelScale = Math.abs(sRotationAccelScale.get());

    this.xSpeed += Math.copySign(
            Math.max(
                    -maxDeceleration * deltaTime,
                    Math.min(
                            maxAcceleration * deltaTime,
                            Math.copySign(xSpeedError, this.xSpeed * xSpeedError)
                    )),
            xSpeedError);
    this.zRotation += Math.copySign(
            Math.max(
                    -maxDeceleration * rotationAccelScale * deltaTime,
                    Math.min(
                            maxAcceleration * rotationAccelScale * deltaTime,
                            Math.copySign(zRotationError, this.zRotation * zRotationError)
                    )),
            zRotationError);

    if (this.xSpeed * xSpeedError > 0) {
      xbox.setRumble(GenericHID.RumbleType.kLeftRumble, Math.abs(xSpeedError) / deltaTime / maxAcceleration);
    }
    else {
      xbox.setRumble(GenericHID.RumbleType.kRightRumble, Math.abs(xSpeedError) / deltaTime / maxDeceleration);
    }

    /*
    if(xSpeedError > 0){
      xbox.setRumble(GenericHID.RumbleType.kRightRumble, xSpeedError);
    }
    else{
      xbox.setRumble(GenericHID.RumbleType.kLeftRumble, xSpeedError);
    }
    */

    drive.arcadeDrive(-this.zRotation, this.xSpeed, false);
  }

  public void arcadeDrive(double zRotation, double xSpeed) {
    arcadeDrive(zRotation, xSpeed, false);
  }

  private double getDecreasing(){
    double finalRate = oldRate - currentRate;
    if(finalRate > 0.05){
      //TODO: implement scaling from 0-1
      return finalRate/4000;
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
