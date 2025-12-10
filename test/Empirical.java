import java.util.ArrayList;
import java.util.List;
import java.util.Random;


  public class Empirical {

    public static void main() {
      System.out.println("Empirical Analysis");
      System.out.println("--------------------------------------------------");

      System.out.println("N,IntervalTree_Time(ns)");

      // test points
      int[] sizes = {10000, 50000, 100000, 500000, 1000000, 2000000, 5000000,10000000,100000000};

      // warm up JVM
      System.err.println("Warming up JVM...");
      runTest(new IntervalSTImpl<>(), 10000, false);

      for (int n : sizes) {
        // every test use a new tree
        IntervalSTImpl<Integer> st = new IntervalSTImpl<>();
        runTest(st, n, true);
      }
    }

    private static void runTest(IntervalSTImpl<Integer> st, int N, boolean print) {
      Random rand = new Random(42);
      List<Interval> querySet = new ArrayList<>();

      // Insert random Intervals
      for (int i = 0; i < N; i++) {
        int lo = rand.nextInt(10000000);
        int hi = lo + rand.nextInt(5000);

        st.put(lo, hi, i);

        // store some existing intervals for laster searching
        if (i % 100 == 0 && querySet.size() < 1000) {
          querySet.add(new Interval(lo, hi));
        }
      }

      // random intervals for searching
      while (querySet.size() < 2000) {
        int lo = rand.nextInt(10000000);
        querySet.add(new Interval(lo, lo + 100));
      }

      // search
      long startSearch = System.nanoTime();
      for (Interval q : querySet) {
        st.get(q.lo, q.hi);
      }
      long endSearch = System.nanoTime();

      // time interval
      long time = (endSearch - startSearch);

      if (print) {
        System.out.printf("%d,%d%n", N, time);
      }
    }
  }

