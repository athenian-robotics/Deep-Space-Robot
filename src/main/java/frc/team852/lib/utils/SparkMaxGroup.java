package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import java.util.ArrayList;
import java.util.Arrays;

import static edu.wpi.first.wpilibj.PIDSourceType.kDisplacement;

public class SparkMaxGroup extends SpeedControllerGroup implements PIDSource, PIDOutput {

  private ArrayList<SparkMax> speedControllerList = new ArrayList<>();
  private PIDSourceType m_sourceType;

  /**
   * @param speedController  Motor Controller using SparkMax speed controller wrapper
   * @param speedControllers The SparkMaxes to add
   */
  public SparkMaxGroup(SparkMax speedController, SparkMax... speedControllers) {
    this(kDisplacement, speedController, speedControllers);
  }

  /**
   * @param sourceType       Read from the encoders in displacement or rate mode
   * @param speedController  Motor controller using SparkMax speed controller wrapper
   * @param speedControllers The SparkMaxes to add
   */
  public SparkMaxGroup(PIDSourceType sourceType, SparkMax speedController, SparkMax... speedControllers) {
    super(speedController, speedControllers);
    speedControllerList.add(speedController);
    speedControllerList.addAll(Arrays.asList(speedControllers));
    m_sourceType = sourceType;
    speedControllerList.forEach(x -> x.setPIDSourceType(m_sourceType));
  }


  public void resetEncoders() {
    speedControllerList.forEach(SparkMax::resetEncoder);
  }

  @Override
  public void setPIDSourceType(PIDSourceType pidSource) {
    m_sourceType = pidSource;
    speedControllerList.forEach(x -> x.setPIDSourceType(m_sourceType));
  }

  @Override
  public PIDSourceType getPIDSourceType() {
    return m_sourceType;
  }

  /**
   * @return The average PID source/output for each SparkMax in the group
   */
  @Override
  public double pidGet() {
    if (speedControllerList.size() == 0)
      return 0;
    double sum = speedControllerList.stream().mapToDouble(SparkMax::pidGet).sum();
    return sum / speedControllerList.size();
  }

  /**
   * @param inverted The inversion value to set for each speed controller in the group
   */
  public void setInverted(boolean inverted) {
    speedControllerList.forEach(sc -> sc.setInverted(inverted));
  }
}
