package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.RobotMap;
import frc.team852.subsystem.Drivetrain;

public class ChangeDriveMode extends Command {

  public ChangeDriveMode() {
  }

  @Override
  protected boolean isFinished() {
    return true;
  }

  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void execute() {
    if (RobotMap.currentDriveMode == Drivetrain.DriveMode.Tank){
      RobotMap.currentDriveMode = Drivetrain.DriveMode.ArcadeJoy;
      System.out.println("Current drive mode = ARCADE-JOYSTICK");
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.ArcadeJoy){
      RobotMap.currentDriveMode = Drivetrain.DriveMode.ArcadePad;
      System.out.println("Current drive mode = ARCADE-GAMEPAD");
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.ArcadePad){
      RobotMap.currentDriveMode = Drivetrain.DriveMode.GTA;
      System.out.println("Current drive mode = GTA");
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.GTA){
      RobotMap.currentDriveMode = Drivetrain.DriveMode.Tank;
      System.out.println("Current drive mode = TANK");
    }
  }
}
