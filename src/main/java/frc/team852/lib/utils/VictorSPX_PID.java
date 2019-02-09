package frc.team852.lib.utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.PIDOutput;

public class VictorSPX_PID extends VictorSPX implements PIDOutput{

  public VictorSPX_PID(int channel, boolean inverted) {
    super(channel);
    this.setInverted(inverted);
  }

  /**
   * @param speed sets speed of motor controller
   */

  public void setSpeed(double speed) {
    this.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Sets speed of motor to 0
   */
  public void stopMotor(){
    this.setSpeed(0);
  }

  /**
   * @return current motor speed (between -1.0 and 1.0)
   */
  public double get(){
    return this.getMotorOutputPercent();
  }

  /**
   * @param speed sets speed of motor controller as a percent (used for PIDOutput)
   */
  public void pidWrite(double speed){
    this.setSpeed(speed);
  }
}