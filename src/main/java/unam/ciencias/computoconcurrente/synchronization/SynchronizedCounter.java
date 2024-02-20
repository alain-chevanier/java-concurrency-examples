package unam.ciencias.computoconcurrente.synchronization;

public class SynchronizedCounter implements Counter {
  int c = 0;

  @Override
  public synchronized int increment() {
    return c++;
  }

  @Override
  public synchronized int decrement() {
    return c--;
  }

  @Override
  public synchronized int value() {
    return c;
  }
}
