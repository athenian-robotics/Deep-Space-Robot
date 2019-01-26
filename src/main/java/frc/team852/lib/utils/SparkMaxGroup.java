package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.SpeedControllerGroup;

import java.util.ArrayList;
import java.util.Arrays;

public class SparkMaxGroup extends SpeedControllerGroup {

  private ArrayList<SparkMax> speedControllerList;

  /**
   * @param speedController  Motor Controller using SparkMax speed controller wrapper
   * @param speedControllers The SpeedControllers to add
   */
  public SparkMaxGroup(SparkMax speedController, SparkMax... speedControllers) {
    super(speedController, speedControllers);
    speedControllerList.add(speedController);
    speedControllerList.addAll(Arrays.asList(speedControllers));
  }

  /**
   * @return The average of all encoder positions within group
   */
  public double getEncoderPos() {
    double val = 0.0;
    for (SparkMax s : speedControllerList) {
      val += s.getEncoderPosition();
    }
    return val / speedControllerList.size();
  }

  public void resetEncoders() {
    for (SparkMax s : speedControllerList) {
      s.resetEncoder();
    }
  }
}
