package unam.ciencias.computoconcurrente.synchronization;

public class SynchronizedCounter {
  int c = 0;

  synchronized void increment() {
    c++;
  }

  synchronized void decrement() {
    c--;
  }

  synchronized int value() {
    return c;
  }
}
