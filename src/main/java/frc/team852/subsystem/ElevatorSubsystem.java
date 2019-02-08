package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.team852.RobotMap;
import frc.team852.command.ElevatorHold;
import frc.team852.lib.utils.SerialLidar;

public class ElevatorSubsystem extends PIDSubsystem {
  private final WPI_TalonSRX motor;
  private final SerialLidar lidar; //TODO replace with i2c lidar when jacob finishes
  private final DigitalInput lowerLimit, upperLimit;

  public ElevatorSubsystem() {
    super("Elevator", 0, 0, 0); // TODO tune
    motor = RobotMap.elevatorMotor;
    lidar = RobotMap.elevatorLidar;
    setPercentTolerance(1);
    getPIDController().setContinuous(false);
    this.lowerLimit = RobotMap.elevatorLowerLimit;
    this.upperLimit = RobotMap.elevatorUpperLimit;
  }
  

  @Override
  protected double returnPIDInput() {
    return lidar.pidGet();
  }

  @Override
  protected void usePIDOutput(double output) {
    if (output > 0 && upperLimit.get()) {
      System.out.println("[!!] Elevator on upper limit.");
      motor.set(0);
    } else if (output < 0 && lowerLimit.get()) {
      System.out.println("[!!] Elevator on lower limit.");
      motor.set(0);
    } else
      motor.set(output);

  }
  public boolean canMoveUp(){
    return !upperLimit.get();
  }
  public boolean canMoveDown(){
    return !lowerLimit.get();
  }

  public boolean canMove(){
    return lowerLimit.get() || upperLimit.get();
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new ElevatorHold());
  }
}
