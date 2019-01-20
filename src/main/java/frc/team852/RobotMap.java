package frc.team852;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.team852.subsystems.NEOTesterSub;
import frc.team852.utils.InvertedDigitalInput;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */
public class RobotMap {
    //Drivetrain Motors
//    public static CANSparkMax leftFront = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
//    public static CANSparkMax leftBack = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
//    public static CANSparkMax rightFront = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
//    public static CANSparkMax rightBack = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);

    //Elevator Motors
    public static CANSparkMax neoTest = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    //public static I2C elevatorDistance = new I2C();
	
	//NEOTesterSub subsystem
	public static NEOTesterSub neoTesterSub = new NEOTesterSub();
}
