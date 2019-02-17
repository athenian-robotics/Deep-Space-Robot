package frc.team852.lib;

import frc.team852.DeepSpaceRobot.*;

import java.util.concurrent.atomic.AtomicReference;

public class CVDataStore {

  public AtomicReference<Ball> ballRef = new AtomicReference<>();
  public AtomicReference<Hatch> hatchRef = new AtomicReference<>();
  public AtomicReference<FrameSize> frameSize = new AtomicReference<>();
  public AtomicReference<ReflTapePair> reflTapeRef = new AtomicReference<>();
  public AtomicReference<GaffeTape> gaffeRef = new AtomicReference<>();
  public AtomicReference<CameraPose> cameraPoseRef = new AtomicReference<>();

}
