package frc.team852;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SerialPort;
import frc.team852.lib.utils.InvertedDigitalInput;
import frc.team852.lib.utils.SparkMax;
import frc.team852.lib.utils.SparkMaxGroup;
import frc.team852.subsystem.*;

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

    //Elevator //TODO Implement elevatorDistanceSensor
    public static ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
    //left and right motors
    private static SparkMax elevatorMotorL = new SparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax elevatorMotorR = new SparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SparkMaxGroup elevatorMotors = new SparkMaxGroup(elevatorMotorL, elevatorMotorR);
    //limit switches
    public static DigitalInput elevatorLowerLimit = new InvertedDigitalInput(0);
    public static DigitalInput elevatorUpperLimit = new InvertedDigitalInput(1);
    public static double elevatorDistanceError = 5;

    //Wrist
    public static WristSubsystem wristSubsystem = new WristSubsystem();
    //wrist motor
    public static SparkMax wristMotor = new SparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
    //limit switches
    public static DigitalInput wristLowerLimit = new InvertedDigitalInput(2);
    public static DigitalInput wristUpperLimit = new InvertedDigitalInput(3);

    //error
    public static SerialPort lidar = new SerialPort(115200, SerialPort.Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);

    //public static I2C elevatorDistance = new I2C();

    //Hatch Subsystem
    public static DoubleSolenoid hatchPancakePneumatics = new DoubleSolenoid(2,3);
    public static HatchSubsystem hatchSubsystem = new HatchSubsystem();

    //Cargo subsystem
    public static CargoSubsystem cargoSubsystem = new CargoSubsystem();

}
