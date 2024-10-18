package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyReentrantLock implements Lock {

  final Lock nonReentrantLock;
  final Condition condition;
  Long holderId;
  int acquiredTimes;

  public MyReentrantLock() {
    this.nonReentrantLock = new NonReentrantLock();
    this.condition = nonReentrantLock.newCondition();
    this.holderId = null;
    this.acquiredTimes = 0;
  }


  @Override
  public void lock() {
    long threadId = Thread.currentThread().getId();
    this.nonReentrantLock.lock();
    try {
      while (this.holderId != null &&
             this.holderId != threadId) {
        this.condition.await(10, TimeUnit.MILLISECONDS);
      }

      this.holderId = threadId;
      this.acquiredTimes++;
    } catch(InterruptedException e) {

    } finally {
      this.nonReentrantLock.unlock();
    }
  }

  @Override
  public void unlock() {
    long threadId = Thread.currentThread().getId();
    this.nonReentrantLock.lock();
    try {
      if (this.holderId != threadId) {
        throw new IllegalArgumentException("....");
      }
      this.acquiredTimes--;
      if (this.acquiredTimes == 0) {
        this.holderId = null;
        this.condition.signal();
      }
    } finally {
      this.nonReentrantLock.unlock();
    }
  }
}
