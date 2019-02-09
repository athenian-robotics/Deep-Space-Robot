package frc.team852.lib.callbacks;

import frc.team852.DeepSpaceRobot.ReflTapePair;

public abstract class ReflTapeListener extends GenericListener<ReflTapePair> {
  public ReflTapeListener() {
    super(ReflTapePair.class);
  }
}
