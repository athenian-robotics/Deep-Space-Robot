package frc.team852;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import frc.team852.lib.utils.*;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */

public class RobotMap {

  //Drivetrain
  private static SparkMax leftFront = new SparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static SparkMax leftBack = new SparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static SparkMax rightFront = new SparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static SparkMax rightBack = new SparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
  public static SparkMaxGroup leftDrive = new SparkMaxGroup(leftFront, leftBack);
  public static SparkMaxGroup rightDrive = new SparkMaxGroup(rightFront, rightBack);
  public static DoubleSolenoid gearbox = new DoubleSolenoid(0, 1);
  public static final DoubleSolenoid.Value HIGH_GEAR = DoubleSolenoid.Value.kForward;
  public static final DoubleSolenoid.Value LOW_GEAR = DoubleSolenoid.Value.kReverse;


  //Elevator
  //private static SparkMax elevatorMotorL = new SparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
  //private static SparkMax elevatorMotorR = new SparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
  //public static SparkMaxGroup elevatorMotors = new SparkMaxGroup(elevatorMotorL, elevatorMotorR);
  public static VictorSPX_PID elevatorMotor = new VictorSPX_PID(4, false);
  public static InvertedDigitalInput elevatorLowerLimit = new InvertedDigitalInput(0);
  public static InvertedDigitalInput elevatorUpperLimit = new InvertedDigitalInput(1);
  public static SerialLidar elevatorLidar = Robot.elevatorLidar;
  //TODO tune pid values
  public static PIDController elevatorPIDPosition = new PIDController(0,0,0, elevatorLidar, elevatorMotor);
  public static PIDController elevatorPIDHold = new PIDController(0,0,0, elevatorLidar, elevatorMotor);


  //Wrist
  public static SparkMax wristMotor = new SparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
  public static DigitalInput wristLowerLimit = new DigitalInput(2);
  public static DigitalInput wristUpperLimit = new DigitalInput(3);
  //TODO tune pid values
  public static PIDController wristPIDPosition = new PIDController(0,0,0, wristMotor, wristMotor);
  public static PIDController wristPIDHold = new PIDController(0,0,0, wristMotor, wristMotor);


  //Cargo subsystem
  public static SparkMax cargoMotor = new SparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);


  //Hatch Subsystem
  public static DoubleSolenoid hatchPancakePneumatics = new DoubleSolenoid(2, 3);


  //Climber Subsystem
  public static SparkMax climberMotor = new SparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless);
  //TODO tune pid values
  public static PIDController climberPIDPosition = new PIDController(0,0,0, climberMotor, climberMotor);
  public static PIDController climberPIDHold = new PIDController(0,0,0, climberMotor, climberMotor);


  public static AHRS gyro = Robot.gyro;
}
