package frc.team852.lib.utils.datatypes;

public interface InverseInterpolatable<T> {
  /**
   * Given lower and upper, estimate how far on
   * [0, 1] between upper and lower the query lies
   * @param upper
   * @param query
   * @return Interpolation param on [0, 1]
   */
  public double inverseInterpolate(T upper, T query);
}
