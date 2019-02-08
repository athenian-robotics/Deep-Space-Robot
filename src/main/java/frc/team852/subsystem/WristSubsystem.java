package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.team852.RobotMap;
import frc.team852.command.WristHold;

public class WristSubsystem extends PIDSubsystem {

  private final WPI_TalonSRX motor;
  private final Encoder encoder;
  private DigitalInput lowerLimit, upperLimit;

  public WristSubsystem() {
    super("Wrist", 0, 0, 0); // TODO Tune
    this.motor = RobotMap.wristMotor;
    this.encoder = RobotMap.wristEncoder;
    this.lowerLimit = RobotMap.wristLowerLimit;
    this.upperLimit = RobotMap.wristUpperLimit;
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new WristHold());
  }

  public void stop() {
    this.motor.stopMotor();
  }

  public double getSpeed() {
    return this.motor.get();
  }

  public void setSpeed(double speed) {
    if (speed > 0 && upperLimit.get()) {
      motor.set(0);
      System.out.println("[!!] Wrist on upper limit.");
    } else if (speed < 0 && lowerLimit.get()) {
      motor.set(0);
      System.out.println("[!!] Wrist on lower limit.");
    } else {
      motor.set(speed);
    }
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
  protected void usePIDOutput(double output) {
    setSpeed(output);
  }

  public double getEncoderPos() {
    return encoder.pidGet();
  }

  public void resetEncoders() {
    encoder.reset();
  }

  @Override
  protected double returnPIDInput() {
    return encoder.get();
  }

}

