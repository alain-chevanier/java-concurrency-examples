package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreImpl implements Semaphore {

  private final int capacity;
  private final Lock lock;
  private final Condition condition;
  private int availableSlots; // value

  private ThreadID threadID;

  public SemaphoreImpl(int capacity) {
    this.capacity = capacity;
    this.lock = new ReentrantLock();
    this.condition = this.lock.newCondition();
    this.availableSlots = this.capacity;
    this.threadID = new ThreadID();
  }

  @Override
  public void down() {
    // lock - acquire
    this.lock.lock();
    try {
      while (availableSlots == 0) {
        try {
          // Opción 1: porque en la parte correspondiente hacen signalAll()
          this.condition.await();
          // Opción 2: aunque en la parte correspondiente hagan signal()
          // this.condition.await(200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      this.availableSlots--;
    } finally {
      this.lock.unlock();
    }
  }

  @Override
  public void up() {
    // unlock - release
    this.lock.lock();
    try {
      this.availableSlots++;
      // Opción 1
      this.condition.signalAll();
      // Opción 2
      // this.condition.signal();
    } finally {
      this.lock.unlock();
    }
  }

  @Override
  public int permits() {
    return this.capacity;
  }

  @Override
  public ThreadID getThreadId() {
    return this.threadID;
  }
}
