import java.util.ArrayList;
import java.util.List;

public class IntervalSTImpl<T> implements IntervalST<T> {
  Node root;
  public IntervalSTImpl() {
    this.root = null;
  }

  // helper function to update Node's max
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

  @Override
  public void put(int lo, int hi, T value) {
    Interval interval = new Interval(lo, hi);
    this.root = putHelper(this.root, interval, value);
  }
  private Node putHelper(Node node, Interval interval, T value) {
    if (node == null) {
      return new Node(interval, value);
    }
    int cmp = interval.compareTo(node.interval);
    if (cmp < 0) {
      node.setLeft(putHelper(node.getLeft(), interval, value));
    }
    else if (cmp > 0) {
      node.setRight(putHelper(node.getRight(), interval, value));
    }
    else {
      node.setValue(value);
    }
    // update max value in the search path
    // before return
    updateNodeMax(node);
    return node;
  }

  @Override
  // get value
  public T get(int lo, int hi) {
    Interval interval = new Interval(lo, hi);
    Node node = getHelper(this.root, interval);
    if (node == null) {
      return null;
    }
    return (T) node.getValue();
  }

  // a helper function that traverse the tree
  // to find the NODE align to the interval
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

  @Override
  public void delete(int lo, int hi) {
    Interval interval = new Interval(lo, hi);
    root = deleteHelper(this.root, interval);
  }

  private Node deleteHelper(Node node, Interval interval) {
    if (node == null) {
      return null;
    }
    int cmp = interval.compareTo(node.interval);
    if (cmp < 0) {
      node.setLeft(deleteHelper(node.getLeft(), interval));
    }
    else if (cmp > 0) {
      node.setRight(deleteHelper(node.getRight(), interval));
    }
    else {
      if (node.getLeft() == null) {
        return node.getRight();
      }
      else if (node.getRight() == null) {
        return node;
      }
      else {
        Node temp = node;
        node = min(temp.getRight());
        node.setRight(deleteMin(temp.getRight()));
        node.setLeft(min(temp.getLeft()));
      }
    }
    updateNodeMax(node);
    return node;

  }

  // find the minimal sub node
  private Node min(Node node) {
    if (node.getLeft() == null) {
      return node;
    }
    return min(node.getLeft());
  }

  // a helper function to delete minium node
  private Node deleteMin(Node node) {
    if (node.getLeft() == null) {
      return node.getRight();
    }
    node.setLeft(deleteMin(node.getLeft()));
    updateNodeMax(node);
    return node;
  }


  @Override
  public Iterable<T> intersect(int lo, int hi) {
    Interval interval = new Interval(lo, hi);
    List<T> list = new ArrayList<>();
    searchHelper(this.root, interval, list);
    return list;
  }

  // helper function to search intersect intervals
  private void searchHelper(Node node, Interval interval, List<T> result) {
    if (node == null) {
      return;
    }

    // if intersets with current node
    if(node.interval.intersects(interval)) {
      result.add((T) node.getValue());
    }

    if(node.getLeft() != null
    && node.getLeft().getMax() > interval.lo) {
      searchHelper(node.getLeft(), interval, result);
    }

    if(node.getRight() != null
            && node.interval.lo <= interval.hi) {
      searchHelper(node.getRight(), interval, result);
    }
  }
}
