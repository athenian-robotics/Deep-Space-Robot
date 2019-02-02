package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import java.util.ArrayList;
import java.util.Arrays;

import static edu.wpi.first.wpilibj.PIDSourceType.kDisplacement;

public class SparkMaxGroup extends SpeedControllerGroup implements PIDOutput, PIDSource {

  private ArrayList<SparkMax> speedControllerList = new ArrayList<>();
  private PIDSourceType sourceType;
  private SparkMax leader;
  private double lastSpeed = 0;

  /**
   * @param speedController  Motor Controller using SparkMax speed controller wrapper
   * @param speedControllers The SparkMaxes to add
   */
  public SparkMaxGroup(SparkMax speedController, SparkMax... speedControllers) {
    this(kDisplacement, speedController, speedControllers);
  }

  /**
   * @param sourceType       Read from the encoders in displacement or rate mode
   * @param leader           Motor controller using SparkMax speed controller wrapper
   * @param speedControllers The SparkMaxes to add
   */
  public SparkMaxGroup(PIDSourceType sourceType, SparkMax leader, SparkMax... speedControllers) {
    super(leader, speedControllers);
//    speedControllerList.add(speedController);
    speedControllerList.addAll(Arrays.asList(speedControllers));
    this.sourceType = sourceType;
    this.leader = leader;
    speedControllerList.forEach(sc -> {
      sc.setPIDSourceType(sourceType);
      sc.follow(this.leader);
    });
  }

  @Override
  public void set(double speed) {
    if (this.lastSpeed != speed) {
      leader.set(speed);
      this.lastSpeed = speed;
    }
  }


  public void resetEncoders() {
    speedControllerList.forEach(SparkMax::resetEncoder);
  }

  @Override
  public void setPIDSourceType(PIDSourceType pidSource) {
    sourceType = pidSource;
    speedControllerList.forEach(x -> x.setPIDSourceType(sourceType));
  }

  @Override
  public PIDSourceType getPIDSourceType() {
    return sourceType;
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
    this.leader.setInverted(inverted);
  }
}
