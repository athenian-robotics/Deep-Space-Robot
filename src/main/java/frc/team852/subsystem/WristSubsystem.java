package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.WristBangBang;

public class WristSubsystem extends Subsystem {

  private final WPI_TalonSRX motor;

  public WristSubsystem() {
    super("Wrist"); // TODO Tune
    this.motor = RobotMap.wristMotor;
    this.motor.setNeutralMode(NeutralMode.Brake);
//    this.setInputRange(ENCODER_RANGE_MIN, ENCODER_RANGE_MAX);
  }

  public void stopMotors() {
    this.motor.stopMotor();
  }

  public double getSpeed() {
    return this.motor.get();
  }

  /**
   * Manually set the speed while paying attention to limit switches in the mechanism
   *
   * @param speed
   */
  public void setSpeed(double speed) {
//    if (speed > 0){ //&& upperLimit.get()) {
//      motor.set();
//      System.out.println("[!!] Wrist on upper limit.");
//    } else if (speed < 0) {
//      motor.set(0);
//      System.out.println("[!!] Wrist on lower limit.");
//    } else {
//      motor.set(speed);
//    }
    motor.set(speed);
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new WristBangBang());
  }


}

