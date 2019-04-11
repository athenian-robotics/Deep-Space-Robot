package frc.team852.subsystem;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.DriveChangeable;
import frc.team852.lib.utils.SparkMax;
import frc.team852.lib.utils.SparkMaxGroup;

public class Drivetrain extends Subsystem {
  public static final double trackDistance = .6112;  // 61.12 cm distance between wheel sides


  private Encoder leftGrayhill = RobotMap.leftGrayhill;
  private Encoder rightGrayhill = RobotMap.rightGrayhill;

  private DoubleSolenoid gearbox = RobotMap.gearbox;
  private DoubleSolenoid.Value gearing = RobotMap.SLOW;

  private final SparkMaxGroup leftDrive = RobotMap.leftDrive;
  private final SparkMaxGroup rightDrive = RobotMap.rightDrive;
  private SparkMax.IdleMode idleMode = SparkMax.IdleMode.kBrake;

  public Drivetrain(){
    super("Drivetrain");
    // Gotta reverse one side of the drivetrain
    leftDrive.setInverted(true);
    leftDrive.setPIDSourceType(PIDSourceType.kDisplacement);
    rightDrive.setPIDSourceType(PIDSourceType.kDisplacement);
    leftDrive.setIdleMode(CANSparkMax.IdleMode.kBrake);
    rightDrive.setIdleMode(CANSparkMax.IdleMode.kBrake);
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveChangeable());
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

  public double getRight() {
    return rightDrive.pidGet();
  }

  public void resetEncoders(){
    rightDrive.resetEncoders();
    leftDrive.resetEncoders();
  }

  // TODO should we use the grayhills for everything and take out the SparkMax encoder methods?
  public double getLeftGrayhill() {
    return leftGrayhill.getDistance();
  }

  public double getRightGrayhill() {
    return rightGrayhill.getDistance();
  }

  public double getDistance() {
    return (leftGrayhill.getDistance() + rightGrayhill.getDistance()) / 2;
  }

  public double getRate() { return (leftGrayhill.getRate() + rightGrayhill.getRate()) / 2;}

  public void resetGrayhills() {
    leftGrayhill.reset();
    rightGrayhill.reset();
  }

  public void stop() {
    leftDrive.set(0);
    rightDrive.set(0);
  }

  public enum DriveMode {
    Tank, GTA, ArcadeJoy, ArcadePad
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

