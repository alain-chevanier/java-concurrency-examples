package unam.ciencias.computoconcurrente.synchronization;

public class ThreadUnsafeCounter implements Counter {
  int c = 0;

  public int increment() {
    return c++;
  }

  public int decrement() {
    return c--;
  }

  public int value() {
    return c;
  }
}
