package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.team852.RobotMap;
import frc.team852.command.ClimberMove;

public class ClimberSubsystem extends PIDSubsystem {

  //Set a climber motor
  private final WPI_TalonSRX climberMotor;
//  private final Encoder encoder;

  //Constructor
  public ClimberSubsystem() {
    super("Climber", 0, 0, 0); // TODO tune
    climberMotor = RobotMap.climberMotor;
//    encoder = RobotMap.climberEncoder;
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
    if (speed != 0)
      this.climberMotor.set(speed);
    else
      stopMotors();
  }

//  public double getEncoderPos() {
//    return this.encoder.get();
//  }

//  public void resetEncoder() {
//    this.encoder.reset();
//  }

  @Override
  protected double returnPIDInput() {
//    return encoder.get();
    return 0;
  }

  @Override
  protected void usePIDOutput(double output) {
    climberMotor.set(output);
  }
}
