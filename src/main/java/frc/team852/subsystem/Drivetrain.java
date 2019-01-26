package frc.team852.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.DriveTank;
import frc.team852.lib.utils.SparkMaxGroup;

public class Drivetrain extends Subsystem {
  private SparkMaxGroup leftDrive = RobotMap.leftDrive;
  private SparkMaxGroup rightDrive = RobotMap.rightDrive;

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveTank());
  }

  public void drive(double leftSpeed, double rightSpeed) {
    // TODO please finish this
    //leftSpeed =
  }


}
