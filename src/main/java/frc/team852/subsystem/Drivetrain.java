package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.command.DriveChangeable;

public class Drivetrain extends Subsystem {
  public static final double trackDistance = .6112;  // 61.12 cm distance between wheel sides


  private Encoder leftEncoder = RobotMap.leftEncoder;
  private Encoder rightEncoder = RobotMap.rightEncoder;

  private final SpeedControllerGroup leftDrive = RobotMap.leftDrive;
  private final SpeedControllerGroup rightDrive = RobotMap.rightDrive;

  public Drivetrain(){
    super("Drivetrain");
    // Gotta reverse one side of the drivetrain
    rightDrive.setInverted(true);
    leftEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
    rightEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
    setNeutralMode(NeutralMode.Brake);
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

  public double getLeft() {
    return leftEncoder.pidGet();
  }

  public double getRight() {
    return rightEncoder.pidGet();
  }

  public void resetEncoders(){
    rightEncoder.reset();
    leftEncoder.reset();
  }

  public double getDistance() {
    return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
  }

  public double getRate() { return (leftEncoder.getRate() + rightEncoder.getRate()) / 2;}

  public void stop() {
    leftDrive.set(0);
    rightDrive.set(0);
  }

  public enum DriveMode {
    Tank, GTA, ArcadeJoy, ArcadePad
  }

  public void setNeutralMode(NeutralMode neutralMode){
    RobotMap.setNeutralMode(neutralMode);
  }

}

