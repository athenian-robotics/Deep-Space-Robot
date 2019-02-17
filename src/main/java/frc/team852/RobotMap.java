package frc.team852;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.team852.subsystem.Drivetrain;

/**
 * Map of all the sensors, motors, and other that the robot uses
 * This provides a lot of flexibility
 */

public class RobotMap {

  //Drivetrain


  private static WPI_VictorSPX leftFront = new WPI_VictorSPX(0);
  private static WPI_VictorSPX leftBack = new WPI_VictorSPX(1);
  private static WPI_VictorSPX rightFront = new WPI_VictorSPX(2);
  private static WPI_VictorSPX rightBack = new WPI_VictorSPX(3);
  public static SpeedControllerGroup leftDrive = new SpeedControllerGroup(leftFront, leftBack);
  public static SpeedControllerGroup rightDrive = new SpeedControllerGroup(rightFront, rightBack);
  public static Encoder leftEncoder = new Encoder(0,1, true);
  public static Encoder rightEncoder = leftEncoder;//new Encoder(2, 3, true);

  public static AHRS gyro = Robot.gyro;

  public static Drivetrain.DriveMode currentDriveMode = Drivetrain.DriveMode.GTA;

  public RobotMap() {
    // TODO confirm values (m/s)
    leftEncoder.setDistancePerPulse(0.0020373563);
    rightEncoder.setDistancePerPulse(0.0020373563);
  }

  public static void setNeutralMode(NeutralMode neutralMode) {
    leftFront.setNeutralMode(neutralMode);
    leftBack.setNeutralMode(neutralMode);
    rightFront.setNeutralMode(neutralMode);
    rightBack.setNeutralMode(neutralMode);
  }
}
