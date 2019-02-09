package frc.team852.lib.utils;

import java.util.LinkedList;

public class LimitedQueue<T> extends LinkedList<T> {
  private int maxSize;

  public LimitedQueue(int maxSize) {
    this.maxSize = maxSize;
  }

  @Override
  public boolean add(T value) {
    boolean retVal = super.add(value);
    if (size() > maxSize)
      removeRange(0, size() - maxSize - 1);
    return retVal;
  }

  public T getYoungest() {
    return get(size() - 1);
  }
}
