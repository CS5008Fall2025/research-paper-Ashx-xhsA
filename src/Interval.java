public class Interval implements Comparable<Interval> {
  public final int lo;
  public final int hi;

  public Interval(int lo, int hi) {
    this.lo = lo;
    this.hi = hi;
  }

  // check if two intervals intersect
  public boolean intersects(Interval other) {
    if (this.hi <other.lo || this.lo > other.hi){
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(Interval o) {
    return this.lo - o.lo;
  }
}
