package frc.team852.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.DriveTank;
import frc.team852.lib.utils.SparkMax;
import frc.team852.lib.utils.SparkMaxGroup;

public class Drivetrain extends Subsystem {
  private SparkMaxGroup leftDrive = RobotMap.leftDrive;
  private SparkMaxGroup rightDrive = RobotMap.rightDrive;
  private DoubleSolenoid gearbox = RobotMap.gearbox;
  private DoubleSolenoid.Value gearing = RobotMap.LOW_GEAR;
  private SparkMax.IdleMode idleMode = SparkMax.IdleMode.kBrake;

  public Drivetrain(){
    super();
    // Gotta reverse one side of the drivetrain
    rightDrive.setInverted(true);
    leftDrive.setIdleMode(CANSparkMax.IdleMode.kBrake);
    rightDrive.setIdleMode(CANSparkMax.IdleMode.kBrake);
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

  public void stop() {
    leftDrive.set(0);
    rightDrive.set(0);
  }

  public void setIdleMode(CANSparkMax.IdleMode idleMode){
    this.idleMode = idleMode;
    leftDrive.setIdleMode(idleMode);
    rightDrive.setIdleMode(idleMode);
  }

  public CANSparkMax.IdleMode getIdleMode(){
    return this.idleMode;
  }


}

