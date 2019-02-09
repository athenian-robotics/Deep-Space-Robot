package frc.team852.lib.callbacks;

public abstract class GenericListener<T extends com.google.protobuf.GeneratedMessageV3> {
  public final Class msgType;

  protected GenericListener(Class msgType) {
    this.msgType = msgType;
  }

  public abstract void onNewData(T data);
}
