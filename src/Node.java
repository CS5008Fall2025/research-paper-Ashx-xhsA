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

  public void setLeft(Node left) {
    this.left = left;
  }
  public void setRight(Node right) {
    this.right = right;
  }
  public Node getLeft() {
    return this.left;
  }
  public Node getRight() {
    return this.right;
  }
  public int getMax() {
    return this.max;
  }
  public void setMax(int max) {
    this.max = max;
  }
  public void setValue(T value) {
    this.value = value;
  }

  public T getValue() {
    return this.value;
  }

}
