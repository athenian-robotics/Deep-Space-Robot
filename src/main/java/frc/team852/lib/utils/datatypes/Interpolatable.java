package frc.team852.lib.utils.datatypes;

public interface Interpolatable<T> {

  /**
   * Interpolates between this and an other value according to the given scale
   * Returns this if x==0 and other if x==1
   * otherwise value will be interpolated proportionally between the two
   * @param other The value of the upper bound
   * @param x Requested value (0 - 1)
   * @return Estimated avg between the surrounding data
   */
  public T interpolate(T other, double x);
}
