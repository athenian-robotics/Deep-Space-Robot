package frc.team852;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.*;
import frc.team852.lib.utils.SerialLidar;
import frc.team852.lib.utils.SparkMax;
import frc.team852.lib.utils.SparkMaxGroup;
import frc.team852.subsystem.Drivetrain;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */

public class RobotMap {

  //Drivetrain
  private static SparkMax leftFront = new SparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless); // TODO set based off of CAN id
  private static SparkMax leftBack = new SparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless); // TODO set based off of CAN id
  private static SparkMax rightFront = new SparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless); // TODO set based off of CAN id
  private static SparkMax rightBack = new SparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless); // TODO set based off of CAN id
  public static SparkMaxGroup leftDrive = new SparkMaxGroup(leftFront, leftBack);
  public static SparkMaxGroup rightDrive = new SparkMaxGroup(rightFront, rightBack);
  public static Encoder leftGrayhill = new Encoder(9, 8, false);
  public static Encoder rightGrayhill = new Encoder(0, 1, true);
  public static DoubleSolenoid gearbox = new DoubleSolenoid(7, 6); // TODO set based off of robot wiring

  //Elevator
  public static SparkMax elevatorMotor = new SparkMax(4, SparkMax.MotorType.kBrushless); // TODO set based off of CAN id
  public static DigitalInput elevatorLowerLimit = new DigitalInput(15); // TODO set based off of robot wiring
  public static DigitalInput elevatorUpperLimit = new DigitalInput(5); // TODO set based off of robot wiring
  public static SerialLidar elevatorLidar = Robot.elevatorLidar;

  //Wrist
  public static WPI_TalonSRX wristMotor = new WPI_TalonSRX(6); // TODO set based off of CAN id

  //Climber Subsystem
  public static WPI_TalonSRX climberMotor = new WPI_TalonSRX(5); // TODO set based off of CAN id
  public static DoubleSolenoid pogoBoi = new DoubleSolenoid(4, 5);
  
  //Ball subsystem
  public static DoubleSolenoid sucker = new DoubleSolenoid(2, 3); //TODO may need to reverse
  public static AHRS gyro = Robot.gyro;


  //Led status
  public static Spark statusLeds = new Spark(9);
  public static DigitalInput inHabRange = new DigitalInput(2);
  public static DigitalInput inStationRange = new DigitalInput(3);

  //Camera Servo
  public static Servo tiltServo = new Servo(8);

  //Constants
  public static DoubleSolenoid.Value FAST = DoubleSolenoid.Value.kReverse;
  public static DoubleSolenoid.Value SLOW = DoubleSolenoid.Value.kForward;
  public static Drivetrain.DriveMode currentDriveMode = Drivetrain.DriveMode.GTA;
  public static boolean ledError = false;
  public static boolean isInOverride = false;

  public RobotMap() {
    // TODO calculate these values (m/s) from gearing, check empirically
    //leftGrayhill.setDistancePerPulse(???);
    //rightGrayhill.setDistancePerPulse(???);
  }
}
