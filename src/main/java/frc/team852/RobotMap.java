package frc.team852;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.team852.lib.utils.SerialLidar;
import frc.team852.subsystem.LimitTesterSub;
import frc.team852.subsystem.NEOTesterSub;

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
    public static CANSparkMax neoTest = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    //public static CANSparkMax neoTest2 = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    //public static I2C elevatorDistance = new I2C();

	//NEOTesterSub subsystem
	public static NEOTesterSub neoTesterSub = new NEOTesterSub();

	//LimitTesterSub subsystem
	public static LimitTesterSub limitTesterSub = new LimitTesterSub(7);

    public static SerialLidar lidar;
   // public static I2C i2cLidar = new I2C(I2C.Port.kOnboard, 0x10);
}
