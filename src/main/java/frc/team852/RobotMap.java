package frc.team852;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team852.lib.utils.InvertedDigitalInput;
import frc.team852.lib.utils.SparkMax;
import frc.team852.subsystem.ElevatorSubsystem;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */
public class RobotMap {
    //Drivetrain Motors
    public static SpeedController leftFront = new SparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SpeedController leftBack = new SparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SpeedController rightFront = new SparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SpeedController rightBack = new SparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);

    //Elevator Motors
    public static ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
    public static SparkMax elevatorMotorL = new SparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SparkMax elevatorMotorR = new SparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static DigitalInput elevatorLowerLimit = new InvertedDigitalInput(0);
    public static DigitalInput elevatorUpperLimit = new InvertedDigitalInput(1);
    public static int elevatorDistanceError = 5;

    //public static I2C elevatorDistance = new I2C();
}
