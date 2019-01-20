package frc.team852;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.team852.utils.InvertedDigitalInput;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */
public class RobotMap {
    //Drivetrain Motors
    public static CANSparkMax leftFront = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static CANSparkMax leftBack = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static CANSparkMax rightFront = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static CANSparkMax rightBack = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);

    //Elevator Motors
    public static CANSparkMax elevatorMotorL = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static CANSparkMax elevatorMotorR = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static DigitalInput elevatorLowerLimit = new InvertedDigitalInput(0);
    public static DigitalInput elevatorUpperLimit = new InvertedDigitalInput(1);
    public static int elevatorDistanceError = 5;

    //public static I2C elevatorDistance = new I2C();
}
