package frc.team852.subsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.ClimberMove;
import frc.team852.lib.utils.SparkMax;

public class ClimberSubsystem extends Subsystem {

  //Set a climber motor
  private final SparkMax climberMotor;
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
    new ClimberMove();
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
