package frc.team852.subsystem;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.command.ElevatorMove;
import frc.team852.lib.utils.SerialLidar;
import frc.team852.lib.utils.Shuffle;
import frc.team852.lib.utils.SparkMax;

public class ElevatorSubsystem extends PIDSubsystem {
  private final SparkMax motor;
  private final DigitalInput lowerLimit, upperLimit;
  private final SerialLidar lidar;

  public ElevatorSubsystem() {
    super("Elevator", 0, 0, 0); // TODO tune
//    disable();
    motor = RobotMap.elevatorMotor;
    motor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    this.lowerLimit = RobotMap.elevatorLowerLimit;
    this.upperLimit = RobotMap.elevatorUpperLimit;
    setPercentTolerance(1);
    setOutputRange(-1, 1);
    getPIDController().setContinuous(false);
//    setInputRange(6, 200);
    lidar = RobotMap.elevatorLidar;
  }

  public static int getHeight() {
    return Robot.elevatorLidar.getLidarDistance();
  }

  public double getOutput() {
    return this.motor.get();
  }

  public void setSpeed(double speed) {
    motor.set(speed);
  }

  @Override
  protected double returnPIDInput() {
    return lidar.pidGet();
  }

  @Override
  protected void usePIDOutput(double output) {
    Shuffle.put(this, "motorPower", output);
    motor.set(output);
  }

  public boolean onUpperLimit() {
    return upperLimit.get();
  }

  public boolean onLowerLimit() {
    return lowerLimit.get();
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new ElevatorMove());
  }
}
