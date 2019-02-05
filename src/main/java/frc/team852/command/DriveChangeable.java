package frc.team852.command;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.Drivetrain;

import static frc.team852.OI.stick1;
import static frc.team852.OI.stick2;
import static frc.team852.OI.xbox;

public class DriveChangeable extends Command {

  private Drivetrain dt = Robot.drivetrain;
  private DifferentialDrive drive = new DifferentialDrive(RobotMap.leftDrive, RobotMap.rightDrive);
  private boolean squareInputs;

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

    SmartDashboard.putNumber("Gyro Angle", Robot.gyro.getAngle());
    SmartDashboard.putNumber("Gyro Fused Heading", Robot.gyro.getFusedHeading());
    SmartDashboard.putNumber("Grayhill Encoder Left (get)", RobotMap.leftGrayhill.get());
    SmartDashboard.putNumber("Grayhill Encoder Left (Distance)", RobotMap.leftGrayhill.getDistance());
    SmartDashboard.putNumber("Grayhill Encoder Left (pid)", RobotMap.leftGrayhill.pidGet());
    SmartDashboard.putNumber("Grayhill Encoder Right (get)", RobotMap.rightGrayhill.get());
    SmartDashboard.putNumber("Grayhill Encoder Right (Distance)", RobotMap.rightGrayhill.getDistance());
    SmartDashboard.putNumber("Grayhill Encoder Right (pid)", RobotMap.rightGrayhill.pidGet());

    SmartDashboard.putNumber("Left Neos", RobotMap.leftDrive.pidGet());
    SmartDashboard.putNumber("Right Neos", RobotMap.rightDrive.pidGet());



    if (RobotMap.currentDriveMode == Drivetrain.DriveMode.Cheezy) {
//      if (stick2.getTriggerPressed()) {
//        drive.curvatureDrive(stick2.getY(), -stick1.getX(), true);
//      } else {
//      drive.curvatureDrive(stick2.getY(), -stick1.getY(), false);
//        System.out.println("QUICK TURN ENABLEEED");
//      }
    }
    else if(RobotMap.currentDriveMode == Drivetrain.DriveMode.CheezyPad){
      if(OI.xboxLB.get() || OI.xboxRB.get()){
        drive.curvatureDrive(-xbox.getX(GenericHID.Hand.kLeft), -xbox.getTriggerAxis(GenericHID.Hand.kLeft) + xbox.getTriggerAxis(GenericHID.Hand.kRight), true);
      System.out.print("Quick Turn is Enabled!");
      }
      else{
        drive.curvatureDrive(-xbox.getX(GenericHID.Hand.kLeft), -xbox.getTriggerAxis(GenericHID.Hand.kLeft) + xbox.getTriggerAxis(GenericHID.Hand.kRight), false);
      }
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.Tank) {
      drive.tankDrive(-stick2.getY(), stick1.getY(), true);
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.ArcadeJoy) {
      drive.arcadeDrive(-stick1.getX(), -stick1.getY(), true);
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.ArcadePad) {
      double multiplyBy = 0.6;
      if (xbox.getTriggerAxis(GenericHID.Hand.kRight) > 0.6)
        multiplyBy = xbox.getTriggerAxis(GenericHID.Hand.kRight);
      drive.arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft) * multiplyBy, -xbox.getY(GenericHID.Hand.kLeft) * multiplyBy, true);
    } else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.GTA) {
      drive.arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft), -xbox.getTriggerAxis(GenericHID.Hand.kLeft) + xbox.getTriggerAxis(GenericHID.Hand.kRight));
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.SmoothedTriggersGTA){
      drive.arcadeDrive(-xbox.getX(GenericHID.Hand.kLeft), -smooth(xbox.getTriggerAxis(GenericHID.Hand.kLeft)) + smooth(xbox.getTriggerAxis(GenericHID.Hand.kRight)));
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.SmoothedTurnGTA){
      drive.arcadeDrive(-smooth(xbox.getX(GenericHID.Hand.kLeft)), -xbox.getTriggerAxis(GenericHID.Hand.kLeft) + xbox.getTriggerAxis(GenericHID.Hand.kRight));
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.SmoothedBothGTA){
      drive.arcadeDrive(-smooth(xbox.getX(GenericHID.Hand.kLeft)), -smooth(xbox.getTriggerAxis(GenericHID.Hand.kLeft)) + smooth(xbox.getTriggerAxis(GenericHID.Hand.kRight)));
    }
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
