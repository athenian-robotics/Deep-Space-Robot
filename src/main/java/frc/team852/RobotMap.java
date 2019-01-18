package frc.team852;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.team852.subsystem.SampleSubsystem;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */
public class RobotMap {
    public static CANSparkMax frontLeft = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static CANSparkMax frontRight = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static CANSparkMax backLeft = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static CANSparkMax backRight = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    public static SampleSubsystem sampleSubsystem = new SampleSubsystem();
    // More stuff

}
