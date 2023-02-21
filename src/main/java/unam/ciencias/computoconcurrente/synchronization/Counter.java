package unam.ciencias.computoconcurrente.synchronization;

public class Counter {
  int c = 0;

  void increment() {
    c++;
  }

  void decrement() {
    c--;
  }

  int value() {
    return c;
  }
}
