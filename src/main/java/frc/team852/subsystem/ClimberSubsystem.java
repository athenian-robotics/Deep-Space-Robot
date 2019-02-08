package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.ClimberMove;
import frc.team852.lib.utils.SparkMax;

public class ClimberSubsystem extends PIDSubsystem {

  //Set a climber motor
  private WPI_TalonSRX climberMotor = RobotMap.climberMotor;
  private Encoder encoder = RobotMap.climberEncoder;

  //Constructor
  public ClimberSubsystem() {
    super("Climber", 0, 0, 0); // TODO tune
  }

  //Default command is climberMove
  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new ClimberMove());
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

  @Override
  protected double returnPIDInput() {
    return encoder.get();
  }

  @Override
  protected void usePIDOutput(double output) {
    climberMotor.set(output);
  }
}
