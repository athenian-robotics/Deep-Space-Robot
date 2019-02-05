package frc.team852.lib.callbacks;

public class CallbackDataContainer<D extends com.google.protobuf.GeneratedMessageV3> {
  private D data;
  public long timestamp;
  public boolean isFresh;

  public CallbackDataContainer() {
    this.isFresh = false;
    this.timestamp = System.currentTimeMillis();
    data = null;
  }

  public void update(D newData) {
    this.data = newData;
    isFresh = true;
    this.timestamp = System.currentTimeMillis();
  }

  public D getData() {
    this.isFresh = false;
    return data;
  }
}
