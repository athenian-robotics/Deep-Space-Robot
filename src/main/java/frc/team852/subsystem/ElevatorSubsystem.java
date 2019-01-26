package frc.team852.subsystem;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.ElevatorPositionHold;
import frc.team852.lib.utils.SparkMaxGroup;

public class ElevatorSubsystem extends Subsystem {

  private SparkMaxGroup elevatorMotors = RobotMap.elevatorMotors;
  private SerialPort lidar = RobotMap.lidar;

  //TODO fix inversion motor & check encoder tick up/down

  public ElevatorSubsystem() {
    super();
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new ElevatorPositionHold());
  }

  public void setSpeed(double speed) {
    this.elevatorMotors.set(speed);
  }

  public void stopMotors() {
    this.elevatorMotors.stopMotor();
  }

  public double getSpeed() {
    return this.elevatorMotors.get();
  }

  public double getEncoderPos() {
    return elevatorMotors.getEncoderPos();
  }

  public void resetEncoders() {
    elevatorMotors.resetEncoders();
  }

  public String getLidarDistance() {
    return lidar.readString();
  }

}
