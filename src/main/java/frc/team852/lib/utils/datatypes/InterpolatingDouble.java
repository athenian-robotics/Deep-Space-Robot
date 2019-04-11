package frc.team852.lib.utils.datatypes;

import org.jetbrains.annotations.NotNull;

public class InterpolatingDouble implements Interpolatable<InterpolatingDouble>, InverseInterpolatable<InterpolatingDouble>, Comparable<InterpolatingDouble> {

  public double value;

  public InterpolatingDouble(double val) {
    value = val;
  }

  @Override
  public InterpolatingDouble interpolate(InterpolatingDouble other, double x) {
    double delta = other.value - value;
    double interp = delta * x + value;
    return new InterpolatingDouble(interp);
  }

  @Override
  public double inverseInterpolate(InterpolatingDouble upper, InterpolatingDouble query) {
    double range = upper.value - value;
    double interpRange = query.value - value;
    if (range <= 0 || interpRange <= 0) {
      return 0;
    }
    return interpRange / range;
  }

  @Override
  public int compareTo(@NotNull InterpolatingDouble o) {
    if (o.value < value)
      return 1;
    if (o.value > value)
      return -1;
    return 0;
  }
}
