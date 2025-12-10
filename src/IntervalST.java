public interface IntervalST<T> {
  // put interval-value pair into ST
  void put(int lo, int hi, T value);

  //value paired with given interval
  T get(int lo, int hi);

  // delete the given interval
  void delete(int lo, int hi);

  //all intervals that intersect
  //the given interval
  Iterable<T> intersect(int lo, int hi);

}
