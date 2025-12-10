[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/zBqi0PeJ)

# Research Paper
* Name: Zhanyi Chen
* Semester: 25 Fall
* Topic: Interval Search Trees



Note the following is an example outline to help you. Please rework as you need, you do not need to follow the section heads and *YOU SHOULD NOT* make everything a bulleted list. This needs to read as an executive report/research paper. 

## Introduction
An interval tree is a tree data structure used to store intervals. Specifically, it efficiently locates all intervals overlapping with any given interval or point. It is commonly used for window queries, such as finding all roads within a rectangular window on a computer map, identifying all visible elements in a 3D scene, or in scheduling systems (to detect time conflicts).

A simple solution involves visiting each interval and testing whether it intersects with a given point or interval, requiring O(n) time where n is the number of intervals in the set. This proves inefficient for large datasets<sup>[1]</sup>.

This report explores the interval search tree, a dynamic data structure based on binary search trees (BSTs), which optimizes such queries to logarithmic time complexity.


## Analysis of Algorithm/Datastructure
Each node in the interval search tree stores the following information<sup>[2]</sup>:

- An interval, represented as a tuple $(lo, hi)$.
- $max$: The maximum $hi$ value in the subtree rooted at this node

The $lo$ values within each range are used as keys to maintain the order of the binary search tree.

### Insertion and Deletion

Insertion and deletion operations are identical to those in self-balancing binary search trees<sup>[3]</sup>, except that the maximum value of each node along the search path must be updated.

### Search

To search for any interval intersecting the query interval $(lo, hi)$, first check whether the current node's interval overlaps the query interval. If an overlap exists, return directly. 

Otherwise, proceed as follows: 

- If the left subtree is null, move to the right. 
- If the left subtree's $max$ endpoint is less than $lo$, move to the right. 
-  In all other cases, move to the left<sup>[4]</sup>.

### Time Complexity

An Interval Search Tree is essentially a binary search tree. Therefore, its speed depends entirely on the height of the tree $H$.

**Insertion**: Inserting a new interval involves two steps. First, starting from the root node, find the appropriate position. This step takes at most the tree's height $H$. The second step involves moving back up the tree, updating the max value of each node along the way. Each update requires comparing three numbers, which is a constant-time operation. Therefore, the total time complexity is proportional to the tree's height, resulting in $O(H)$.

**Search**: During the search operation, the algorithm makes that only one decision needs to be made at each node: left, right, or return. Therefore, at most, it only takes one path from the root node to a leaf node. The path length is also the height of the tree.

If the insertion intervals are in random order, the tree will be relatively balanced. The height $H$ is approximately equal to $log N$ (where N is the total number of nodes). Therefore, under normal circumstances, the complexity of insertion, search, and deletion is $O(log N)$. However, if a BST is used without a balancing mechanism and the data is sorted, it will degenerate into a linked list, where $H = N$, and the time complexity degrades to $O(N)$.


## Implementation
I implemented an interval search tree using Java, creating an `Interval` class to represent line segments and a `Node` class to represent tree nodes. This includes comprehensive JUnit tests.

**1. Interval Class**

The interval class provides built-in methods to compare whether two interval instances intersect. This demonstrates code encapsulation, ensuring the main program only needs to input the two endpoints of a line segment without concerning itself with how the line segment is implemented.

```java
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

```

**2. Node Class**

The Node class stores information about intervals and their associated values. It also maintains a record of the maximum right endpoint of its child nodes.

```java
public class Node<T> {
  public final Interval interval;
  private T value;
  private Node left;
  private Node right;
  private int max;

  public Node(Interval interval, T value) {
    this.interval = interval;
    this.value = value;
    this.max = this.interval.hi;
  }

```

**3. UpdateNodeMax**

Compare the right endpoints of the intervals for the root node and its left and right child nodes, then update the $max$ value for each node from bottom to top.

```java
private void updateNodeMax(Node node) {
    int newMax = node.getMax();
    if (node == null) {
      return;
    }
    if (node.getLeft() != null) {
      newMax = Math.max(node.getMax(), node.getLeft().getMax());
    }
    if (node.getRight() != null) {
      newMax = Math.max(node.getMax(), node.getRight().getMax());
    }
    node.setMax(newMax);
  }
```

This is the core part of the intersect function. This helper function locates intervals that overlap with a target interval. The implementation primarily relies on:

1. If the left subtree is non-empty and its max value is less than the target interval's lower bound ($lo$), then the desired node definitely does not exist in the left subtree. We can thus safely search the right subtree.
2. If condition 1 is not satisfied, we can also conclude that there is either an intersection in the left subtree or no intersections in either subtree. This is because if the left subtree cannot find the target node, it means the left subtree's lower bound ($lo$) is greater than the target interval's $hi$. Consequently, the right subtree's lower bound ($lo$) is even greater than the interval's $hi$.

```java
  private Node getHelper(Node node, Interval interval) {
    while (node != null) {
      // if intersects
      if(node.interval.intersects(interval)) {return node;}

      // if left node is null
      else if(node.getLeft() == null) {
        // traverse to right
        node = node.getRight();
      }
      // if left node's max < target's lo
      else if(node.getLeft().getMax() < interval.lo){
        // traverse to right
        node = node.getRight();
      }

      else node = node.getLeft();
    }
    return null;
  }

```

## Empirical Analysis

I implemented the generation of Empirical Analysis data in Empirical.java. I created Interval Search Trees of varying sizes ($N$) ranging from 10,000 to 100,000,000. Each tree performed 2,000 searches. Within the search list, 1,000 data points were guaranteed to exist within the tree, while 1,000 were randomly generated and not guaranteed to be present. When $N$ is small, we can assume these 2,000 data points are entirely non-existent. However, as $N$ increases significantly, the probability of these random data points being included among the actual data also rises. This occurs because the average length of each real node's interval increases at this scale. Consequently, some experimental data may not align with expectations.

The following are the experimental data:

|     N     | Time(ns) |
| :-------: | :------: |
|   10000   |  280625  |
|   50000   |  272083  |
|  100000   |  197458  |
|  500000   |  245000  |
|  1000000  |  280625  |
|  2000000  |  256792  |
|  5000000  |  304583  |
| 10000000  |  332458  |
| 100000000 |  374125  |

When N increases by a factor of 10,000, the query time only increases by approximately 33%. This aligns with logarithmic growth behavior.

![image-20251210080732214](/Users/mineral/Desktop/research-paper-Ashx-xhsA/Data.png)

Taking the logarithm of N and performing linear regression yields the fitting equation:

$$time = 26889 × log₁₀(N) + 119005$$

$R² ≈ 0.68 $

![image-20251210082534127](/Users/mineral/Desktop/research-paper-Ashx-xhsA/Linear.png)

The fit is moderately good, showing a linear growth trend.


## Summary
The $O(logn)$ complexity demonstrates significantly superior efficiency. It continues to perform well even when confronted with massive datasets. However, discrepancies persist between theoretical predictions and observed data, potentially attributable to JVM warm-up effects and pseudo-random datasets.

# References

[1] Wikipedia contributors. (2025, December 5). *Interval tree*. Wikipedia. https://en.wikipedia.org/wiki/Interval_tree

[2] GeeksforGeeks. (2025, July 23). *Interval tree*. GeeksforGeeks. https://www.geeksforgeeks.org/dsa/interval-tree/

[3]Anand, A. (n.d.). *Interval tree*. Astik Anand. https://astikanand.github.io/techblogs/advanced-data-structures/interval-tree

[4]*COS 226 lectures (Fall 2025)*. (n.d.). https://www.cs.princeton.edu/courses/archive/fall25/cos226/lectures.php