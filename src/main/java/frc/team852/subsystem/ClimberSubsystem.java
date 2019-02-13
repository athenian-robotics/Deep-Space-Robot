package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.ClimberMove;

public class ClimberSubsystem extends Subsystem {

  //Set a climber motor
  private final WPI_TalonSRX climberMotor;
  private final Encoder encoder;

  //Constructor
  public ClimberSubsystem() {
    super("Climber");
    climberMotor = RobotMap.climberMotor;
    encoder = RobotMap.climberEncoder;
  }

  //Default command is climberMove
  @Override
  protected void initDefaultCommand() {
  }

  public void stopMotors() {
    this.climberMotor.stopMotor();
  }

  public double getSpeed() {
    return this.climberMotor.get();
  }

  public void setSpeed(double speed) {
    this.climberMotor.set(speed);
  }

  public double getEncoderPos() {
    return this.encoder.get();
  }

  public void resetEncoder() {
    this.encoder.reset();
  }
}
