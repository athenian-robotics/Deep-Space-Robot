package frc.team852.lib.utils.datatypes;

import java.util.Map;
import java.util.TreeMap;

public class InterpolatingTreeMap<K extends InverseInterpolatable<K> & Comparable<K>, V extends Interpolatable<V>> extends TreeMap<K, V> {
  int maxSize;

  public InterpolatingTreeMap(int maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * Create an InterpolatingTreeMap w/no size limit
   */
  public InterpolatingTreeMap() {
    this(0);
  }

  @Override
  public V put(K key, V val) {
    if (maxSize > 0 && maxSize <= size()) {
      K first = firstKey(); // Trim the map
      remove(first);
    }
    super.put(key, val);
    return val;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    System.out.println("Unimplemented method");
  }

  public V getInterpolated(K key) {
    V gotVal = get(key);
    if (gotVal == null) {
      K upperBound = ceilingKey(key);
      K lowerBound = floorKey(key);
      if (upperBound == null && lowerBound == null)
        return null;
      else if (upperBound == null)
        return get(lowerBound);
      else if (lowerBound == null)
        return get(upperBound);
      else {
        V upperVal = get(upperBound);
        V lowerVal = get(lowerBound);
        return lowerVal.interpolate(upperVal, lowerBound.inverseInterpolate(upperBound, key));
      }
    } else {
      return gotVal;
    }
  }
}
