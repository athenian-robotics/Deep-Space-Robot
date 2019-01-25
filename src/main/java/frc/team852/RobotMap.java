package frc.team852;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.team852.lib.utils.InvertedDigitalInput;
import frc.team852.lib.utils.SparkMax;
import frc.team852.lib.utils.SparkMaxGroup;
import frc.team852.subsystem.ElevatorSubsystem;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */
public class RobotMap {
    //Drivetrain Motors
    private static SparkMax leftFront = new SparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax leftBack = new SparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax rightFront = new SparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax rightBack = new SparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMaxGroup leftDrive = new SparkMaxGroup(leftFront, leftBack);
    private static SparkMaxGroup rightDrive = new SparkMaxGroup(rightFront, rightBack);
    public static DifferentialDrive drive = new DifferentialDrive(leftDrive, rightDrive);

    //Elevator Motors //TODO Implement elevatorDistanceSensor
    public static ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
    private static SparkMax elevatorMotorL = new SparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static SparkMax elevatorMotorR = new SparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SparkMaxGroup elevatorMotors = new SparkMaxGroup(elevatorMotorL, elevatorMotorR);
    public static DigitalInput elevatorLowerLimit = new InvertedDigitalInput(0);
    public static DigitalInput elevatorUpperLimit = new InvertedDigitalInput(1);
    public static double elevatorDistanceError = 5;


    public static SerialPort lidar = new SerialPort(115200, SerialPort.Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
    //public static I2C elevatorDistance = new I2C();

}
