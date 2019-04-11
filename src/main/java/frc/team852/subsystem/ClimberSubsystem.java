package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.ClimberMove;

public class ClimberSubsystem extends Subsystem {

  //Set a climber motor
  private final WPI_TalonSRX climberMotor;
  private final DoubleSolenoid pogo;

  //Constructor
  public ClimberSubsystem() {
    super("Climber"); // TODO tune
    climberMotor = RobotMap.climberMotor;
    climberMotor.setInverted(true);
    pogo = RobotMap.pogoBoi;
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

  public void extendPogo() {
    pogo.set(DoubleSolenoid.Value.kForward);
  }

  public void retractPogo() {
    pogo.set(DoubleSolenoid.Value.kReverse);
  }

  public DoubleSolenoid.Value getPogoState(){
    return pogo.get();
  }


}
