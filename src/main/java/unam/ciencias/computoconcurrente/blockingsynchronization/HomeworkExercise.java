package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HomeworkExercise {
  static Lock mutex = new ReentrantLock();
  static int counter = 0;
  static Condition onZero = mutex.newCondition();

  public static void main(String[] args) throws Exception {
    List<Thread> threads =
      List.of(new Thread(() -> decreaseCounter()),
              new Thread(() -> decreaseCounter()),
              new Thread(() -> increaseCounter()),
              new Thread(() -> increaseCounter()));
    for (var t : threads) {
      t.start();
    }
    for (var t : threads) {
      t.join();
    }
  }

  static void decreaseCounter() {
    mutex.lock();
    try {
      if (counter == 0) {
        onZero.await();
      }
      counter--;
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      mutex.unlock();
    }
  }

  static void increaseCounter() {
    mutex.lock();
    try {
      if (counter == 1) {
        onZero.signal();
      }
      counter++;
    } finally {
      mutex.unlock();
    }
  }
}
