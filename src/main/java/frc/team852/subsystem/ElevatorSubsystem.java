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
    getPIDController().setContinuous(false);
    lidar = RobotMap.elevatorLidar;


//    setInputRange(INPUT_RANGE_MIN, INPUT_RANGE_MAX);

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
    if (output > 0 && upperLimit.get()) {
      System.out.println("[!!] Elevator on upper limit.");
      output = 0;
    } else if (output < 0 && lowerLimit.get()) {
      System.out.println("[!!] Elevator on lower limit.");
      output = 0;
    }
    Shuffle.put(this, "motorPower", output);
    motor.set(output);

  }

  public boolean canMoveUp() {
    return !upperLimit.get();
  }

  public boolean canMoveDown() {
    return !lowerLimit.get();
  }

  public boolean canMove() {
    return lowerLimit.get() || upperLimit.get();
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new ElevatorMove());
  }
}
