package frc.team852.lib.callbacks;

import com.google.protobuf.Empty;
import frc.team852.DeepSpaceRobot.CameraPose;

public abstract class CameraPoseListener extends GenericListener<CameraPose, Empty> {
  public CameraPoseListener() {
    super(CameraPoseListener.class);
  }
}
