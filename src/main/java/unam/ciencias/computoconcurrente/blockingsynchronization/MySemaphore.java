package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MySemaphore implements Semaphore {

  int value;
  Lock lock;
  Condition condition;

  public MySemaphore(int initialValue) {
    this.value = initialValue;
    this.lock = new ReentrantLock();
    this.condition = this.lock.newCondition();
  }

  public MySemaphore() {
    this(0);
  }

  @Override
  public void down() {
    this.lock.lock();
    try {
      // what if value is <= 0??
      while (this.value <= 0) {
        this.condition.await(10, TimeUnit.MILLISECONDS);
      }
      this.value--;
    } finally {
      this.lock.unlock();
    }
  }

  @Override
  public void up() {

  }

}
