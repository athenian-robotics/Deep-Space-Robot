package frc.team852.lib.utils;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

public class SparkMax extends CANSparkMax {

  private double resetOffset, lastPos, val;
  private CANEncoder enc;

  public SparkMax(int channel, MotorType motorType) {
    super(channel, motorType);
    resetOffset = 0.0;
    lastPos = 0.0;
    val = 0.0;
    enc = getEncoder();
  }

  /**
   * @return Readings from the encoder
   */
  public double getEncoderPosition() {
    lastPos = val;
    val = enc.getPosition();
    if (resetOffset >= val - 0.05 && resetOffset <= val + 0.05) return 0.0000;
    if (val == 0.0) return lastPos - resetOffset;
    return val - resetOffset;
  }

  /**
   * Compensate for hardware drift
   */
  public void resetEncoder() {
    resetOffset += getEncoderPosition();
  }
}