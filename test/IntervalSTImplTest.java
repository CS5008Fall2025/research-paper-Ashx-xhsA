import static org.junit.jupiter.api.Assertions.*;

class IntervalSTImplTest {
  public static void main(String[] args) {
    testBasicPutAndGet();
    testSearchAllIntersections();
    testDeletion();
  }

  // test insertion and search
  public static void testBasicPutAndGet() {
    System.out.println("Running Test 1: Basic Put and Get...");
    IntervalSTImpl<String> st = new IntervalSTImpl<>();

    st.put(17, 19, "Interval A");
    st.put(5, 8, "Interval B");
    st.put(21, 24, "Interval C");
    st.put(4, 8, "Interval D");
    st.put(15, 18, "Interval E");
    st.put(7, 10, "Interval F");


    String result = st.get(23, 25);
    if ("Interval C".equals(result)) System.out.println("  [PASS] Found overlapping interval: " + result);
    else System.out.println("  [FAIL] Expected Interval C, got " + result);

    // Test non-existent
    String empty = st.get(100, 101);
    if (empty == null) System.out.println("  [PASS] Correctly returned null for no overlap.");
    else System.out.println("  [FAIL] Found phantom interval.");
  }

  // test all overlapping intervals
  public static void testSearchAllIntersections() {
    System.out.println("\nRunning Test 2: Intersect All...");
    IntervalSTImpl<String> st = new IntervalSTImpl<>();

    st.put(10, 20, "Target 1");
    st.put(15, 25, "Target 2"); // overlaps 10-20
    st.put(30, 40, "No Match");
    st.put(5, 12, "Target 3");  // overlaps 10-20

    // Search for [11, 13] -> Should match Target 1, Target 3
    System.out.println("  Querying [11, 13]...");
    Iterable<String> results = st.intersect(11, 13);
    int count = 0;
    for (String s : results) {
      System.out.println("  -> Found: " + s);
      count++;
    }

    if (count == 2) System.out.println("  [PASS] Found correct number of intersections.");
    else System.out.println("  [FAIL] Expected 2, found " + count);
  }

  // test deletion
  public static void testDeletion() {
    System.out.println("\nRunning Test 3: Deletion...");
    IntervalSTImpl<String> st = new IntervalSTImpl<>();
    st.put(10, 20, "To Delete");
    st.put(30, 40, "Remain");

    // Verify exists
    if (st.get(12, 13) != null) System.out.println("  [PASS] Item exists before delete.");

    // Delete
    st.delete(10, 20);

    // Verify gone
    if (st.get(12, 13) == null) System.out.println("  [PASS] Item deleted successfully.");
    else System.out.println("  [FAIL] Item still exists after delete.");
  }



}