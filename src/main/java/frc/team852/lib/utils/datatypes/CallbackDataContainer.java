package frc.team852.lib.utils.datatypes;

import edu.wpi.first.wpilibj.Timer;

public class CallbackDataContainer<D extends com.google.protobuf.GeneratedMessageV3> {
  private D data;
  public double timestamp;
  public boolean isFresh;

  public CallbackDataContainer() {
    this.isFresh = false;
    this.timestamp = Timer.getFPGATimestamp();
    data = null;
  }

  public void update(D newData) {
    this.data = newData;
    isFresh = true;
    this.timestamp = Timer.getFPGATimestamp();
  }

  public D getData() {
    this.isFresh = false;
    return data;
  }
}
