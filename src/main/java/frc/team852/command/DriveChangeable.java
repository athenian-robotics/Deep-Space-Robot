package frc.team852.command;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.team852.OI;
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
  private static final Shuffle sRotationAccelScale = new Shuffle(DriveChangeable.class, "rotationAccelScale", 0.5);
  private static final Shuffle sRotationMax = new Shuffle(DriveChangeable.class, "rotationMax", 0.5);
  private static final Shuffle sElevatorScale = new Shuffle(DriveChangeable.class, "elevatorScale", 1);

  public DriveChangeable() {
    requires(Robot.drivetrain);
  }

  @Override
  protected void initialize() {
    new DriveLogging().start();
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
      squareInputs = OI.xboxStart.get();
      if(squareInputs)
        System.out.println("SQUARING INPUTS!");
      arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft), -xbox.getTriggerAxis(GenericHID.Hand.kLeft) + xbox.getTriggerAxis(GenericHID.Hand.kRight), squareInputs);
    }
  }

  // TODO migrate to a more sensible and general place
  public void arcadeDrive(double zRotation, double xSpeed, boolean squareInputs) {
    boolean fastGear = (Robot.drivetrain.getGearing() == RobotMap.FAST);
    double rotationMax = sRotationMax.get();
    zRotation *= rotationMax + (1 - rotationMax) * Math.abs(xSpeed);
    if (fastGear) zRotation = Math.copySign(Math.min(Math.abs(zRotation), Math.abs(xSpeed)), zRotation);

    double currTime = System.currentTimeMillis();
    double deltaTime = (currTime - lastTime) / 1000d;
    lastTime = currTime;

//    if (squareInputs) {
//      this.xSpeed = Math.copySign(this.xSpeed * this.xSpeed, this.xSpeed);
//      this.zRotation = Math.copySign(this.zRotation * this.zRotation, this.zRotation);
//    }

    double xSpeedError = xSpeed - this.xSpeed;
    double zRotationError = zRotation - this.zRotation;

    double maxAcceleration, maxDeceleration;
    maxAcceleration = Math.abs(fastGear ? sMaxAccelFast.get() : sMaxAccelSlow.get());
    maxDeceleration = Math.abs(fastGear ? sMaxDecelFast.get() : sMaxDecelSlow.get());
    double rotationAccelScale = Math.abs(sRotationAccelScale.get());
    if (fastGear) ;

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

    if (this.xSpeed * xSpeedError < 0) {
      xbox.setRumble(GenericHID.RumbleType.kRightRumble, Math.abs(xSpeedError) / deltaTime / maxDeceleration);
    }
    else {
      xbox.setRumble(GenericHID.RumbleType.kRightRumble, 0);
    }

    /*
    if(xSpeedError > 0){
      xbox.setRumble(GenericHID.RumbleType.kRightRumble, xSpeedError);
    }
    else{
      xbox.setRumble(GenericHID.RumbleType.kLeftRumble, xSpeedError);
    }
    */

    drive.arcadeDrive(-this.zRotation, this.xSpeed, squareInputs);
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

  protected double limit(double value, double limit) {
    if (value > limit) {
      return limit;
    }
    if (value < limit * -1.0) {
      return limit * -1.0;
    }
    return value;
  }
}
