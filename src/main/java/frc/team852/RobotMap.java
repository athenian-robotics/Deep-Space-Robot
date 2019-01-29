package frc.team852;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.team852.lib.utils.SparkMax;
import frc.team852.lib.utils.SparkMaxGroup;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */

public class RobotMap {

    //Drivetrain
    public static Drivetrain driveSubsystem = new Drivetrain();
    //Drivetrain Motors
    private static SparkMax leftFront = new SparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax leftBack = new SparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax rightFront = new SparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax rightBack = new SparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SparkMaxGroup leftDrive = new SparkMaxGroup(leftFront, leftBack);
    public static SparkMaxGroup rightDrive = new SparkMaxGroup(rightFront, rightBack);
    public static Encoder test = new Encoder(0,1);


    //Elevator
    public static ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
    //left and right motors
    private static SparkMax elevatorMotorL = new SparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax elevatorMotorR = new SparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SparkMaxGroup elevatorMotors = new SparkMaxGroup(elevatorMotorL, elevatorMotorR);
    //limit switches
    public static DigitalInput elevatorLowerLimit = new InvertedDigitalInput(0);
    public static DigitalInput elevatorUpperLimit = new InvertedDigitalInput(1);
    //Lidar
    public static SerialLidar lidar = new SerialLidar(115200, SerialPort.Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);

    public static double elevatorDistanceError = 5;


    //Wrist
    public static WristSubsystem wristSubsystem = new WristSubsystem();
    //wrist motor
    public static SparkMax wristMotor = new SparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
    //limit switches
    public static DigitalInput wristLowerLimit = new InvertedDigitalInput(2);
    public static DigitalInput wristUpperLimit = new InvertedDigitalInput(3);
    //public static I2C elevatorDistance = new I2C();


    //Cargo subsystem
    public static SparkMax cargoMotor = new SparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static CargoSubsystem cargoSubsystem = new CargoSubsystem();

    //Hatch Subsystem
    public static DoubleSolenoid hatchPancakePneumatics = new DoubleSolenoid(2,3);
    public static HatchSubsystem hatchSubsystem = new HatchSubsystem();

    //Climber Subsystem
    public static SparkMax climberMotor = new SparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static ClimberSubsystem climberSubsystem = new ClimberSubsystem();

    public static AHRS gyroscope = Robot.gyro;
}
