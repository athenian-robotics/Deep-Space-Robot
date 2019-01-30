package frc.team852.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.DriveTank;
import frc.team852.lib.utils.SparkMaxGroup;

public class Drivetrain extends Subsystem {
  private SparkMaxGroup leftDrive = RobotMap.leftDrive;
  private SparkMaxGroup rightDrive = RobotMap.rightDrive;
  private DoubleSolenoid gearbox = RobotMap.gearbox;
  private DoubleSolenoid.Value gearing = RobotMap.LOW_GEAR;

  public Drivetrain(){
    super();
    // Gotta reverse one side of the drivetrain
    rightDrive.setInverted(true);
    leftDrive.setPIDSourceType(PIDSourceType.kDisplacement);
    rightDrive.setPIDSourceType(PIDSourceType.kDisplacement);
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveTank());
  }

  public void drive(double leftSpeed, double rightSpeed) {
    drive(leftSpeed, rightSpeed, false);
  }

  public void drive(double leftSpeed, double rightSpeed, boolean squareInputs) {
    leftDrive.set(leftSpeed);
    rightDrive.set(rightSpeed);
  }

  public DoubleSolenoid.Value getGearing() {
    return gearing;
  }

  public void setGearbox(DoubleSolenoid.Value m_gearing) {
    if (m_gearing == gearing)
      return;
    gearbox.set(m_gearing);
    gearing = m_gearing;
  }

  public double getLeft() {
    return leftDrive.pidGet();
  }

  public void resetEncoders(){
    rightDrive.resetEncoders();
    leftDrive.resetEncoders();
  }
  public double getRight() {
    return rightDrive.pidGet();
  }

  public void stop() {
    leftDrive.set(0);
    rightDrive.set(0);
  }

}

