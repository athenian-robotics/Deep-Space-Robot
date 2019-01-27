package frc.team852;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.team852.lib.utils.SparkMax;
import frc.team852.lib.utils.SparkMaxGroup;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */
public class RobotMap {

  // Drivetrain Stuff
  private static SparkMax leftFront = new SparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static SparkMax leftBack = new SparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static SparkMax rightFront = new SparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static SparkMax rightBack = new SparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
  public static SparkMaxGroup leftDrive = new SparkMaxGroup(leftFront, leftBack);
  public static SparkMaxGroup rightDrive = new SparkMaxGroup(rightFront, rightBack);

  public static DoubleSolenoid gearbox = new DoubleSolenoid(0, 1);
  public static DoubleSolenoid.Value LOW_GEAR = DoubleSolenoid.Value.kForward; // Not 100% sure of this
  public static DoubleSolenoid.Value HIGH_GEAR = DoubleSolenoid.Value.kReverse;
}
