package frc.team852.lib;

import frc.team852.DeepSpaceRobot.Ball;
import frc.team852.DeepSpaceRobot.CVData;
import frc.team852.DeepSpaceRobot.FrameSize;
import frc.team852.DeepSpaceRobot.Hatch;

import java.util.concurrent.atomic.AtomicReference;

public class CVDataStore {

  public AtomicReference<Ball> ballRef = new AtomicReference<>();
  public AtomicReference<Hatch> hatchRef = new AtomicReference<>();
  public AtomicReference<FrameSize> frameSize = new AtomicReference<>();
  public AtomicReference<CVData> dataRef = new AtomicReference<>();





}
