package frc.team852.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.command.SubsystemPID;
import frc.team852.lib.utils.SparkMaxGroup;

public class ElevatorSubsystem extends Subsystem {

  private SparkMaxGroup elevatorMotors = RobotMap.elevatorMotors;

  //TODO fix inversion motor & check encoder tick up/down

  public ElevatorSubsystem() {
    super();
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new SubsystemPID(RobotMap.elevatorLidar.getLidarDistance()[0], RobotMap.elevatorPIDHold, Robot.elevatorSubsystem, 2, RobotMap.elevatorLowerLimit, RobotMap.elevatorUpperLimit, true, RobotMap.elevatorLidar));
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
    return elevatorMotors.pidGet();
  }

  public void resetEncoders() {
    elevatorMotors.resetEncoders();
  }

  //Returns an Int array with [distance,strength]
  public int[] getLidarDistance() {
    return RobotMap.elevatorLidar.getLidarDistance();
  }

}
