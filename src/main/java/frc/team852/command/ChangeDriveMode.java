package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.RobotMap;
import frc.team852.subsystem.Drivetrain;

public class ChangeDriveMode extends Command {

  public ChangeDriveMode() {
  }

  /**
   *
   * @return false 'cuz stopping the drivetrain would be stupid
   */
  @Override
  protected boolean isFinished() {
    return true;
  }

  /**
   * If it is told to stop, stop gracefully
   */
  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void execute() {
    if (RobotMap.currentDriveMode == Drivetrain.DriveMode.Tank) {
      RobotMap.currentDriveMode = Drivetrain.DriveMode.Cheezy;
      System.out.println("Current drive mode = CHEEZY-DRIVE");
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.Cheezy){
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
      RobotMap.currentDriveMode = Drivetrain.DriveMode.CheezyPad;
      System.out.println("Current drive mode = CHEEZY-PAD");
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.CheezyPad){
      RobotMap.currentDriveMode = Drivetrain.DriveMode.SmoothedTriggersGTA;
      System.out.println("Current drive mode = SMOOTHED-TRIGGERS-GTA");
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.SmoothedTriggersGTA){
      RobotMap.currentDriveMode = Drivetrain.DriveMode.SmoothedTurnGTA;
      System.out.println("Current drive mode = SMOOTHED-TURN-GTA");
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.SmoothedTurnGTA){
      RobotMap.currentDriveMode = Drivetrain.DriveMode.SmoothedBothGTA;
      System.out.println("Current drive mode = SMOOTHED-BOTH-GTA");
    }
    else if (RobotMap.currentDriveMode == Drivetrain.DriveMode.SmoothedBothGTA){
      RobotMap.currentDriveMode = Drivetrain.DriveMode.Tank;
      System.out.println("Current drive mode = TANK");
    }
  }
}
