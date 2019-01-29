package frc.team852.lib.utils;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class SparkMax extends CANSparkMax implements PIDSource, PIDOutput{

  private double resetOffset, lastPos, val;
  private CANEncoder enc;
  private PIDSourceType m_sourceType;

  /**
   * <p>Defaults to displacement readings for the encoder</p>
   *
   * @param channel   CAN id of the SparkMax
   * @param motorType Brushed or Brushless motor connected (Really important)
   */
  public SparkMax(int channel, MotorType motorType) {
    this(channel, motorType, PIDSourceType.kDisplacement);
  }

  /**
   * @param channel    CAN id of the SparkMax
   * @param motorType  Brushed or Brushless motor connected (Really important)
   * @param sourceType Read from the encoder in displacement or rate mode
   */
  public SparkMax(int channel, MotorType motorType, PIDSourceType sourceType) {
    this(channel, motorType, sourceType, false);
  }
  /**
   * @param channel    CAN id of the SparkMax
   * @param motorType  Brushed or Brushless motor connected (Really important)
   * @param sourceType Read from the encoder in displacement or rate mode
   * @param inverted   Reverse the motor direction if true
   */
  public SparkMax(int channel, MotorType motorType, PIDSourceType sourceType, boolean inverted) {
    super(channel, motorType);
    this.resetOffset = 0.0;
    this.lastPos = 0.0;
    this.val = 0.0;
    this.enc = getEncoder();
    this.m_sourceType = sourceType;
    this.setInverted(inverted);
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
   * <p>Compensate for hardware drift</p>
   */
  public void resetEncoder() {
    resetOffset += getEncoderPosition();
  }

  /**
   * @param pidSource The new source type (displacement or rate)
   */
  @Override
  public void setPIDSourceType(PIDSourceType pidSource) {
    this.m_sourceType = pidSource;
  }

  /**
   * @return m_sourceType
   */
  @Override
  public PIDSourceType getPIDSourceType() {
    return m_sourceType;
  }

  /**
   * @return The reading of SparkMax's encoder in accordance with the @see edu.wpi.first.wpilibj.PIDSourceType specified (defaults to displacement)
   */
  @Override
  public double pidGet() {
    switch (m_sourceType) {
      case kDisplacement:
        return getEncoderPosition();
      case kRate:
        return enc.getVelocity();
      default:
        return 0.0;
    }
  }
}