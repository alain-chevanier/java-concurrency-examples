package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Nota: esta implementación permite que los writers progresen
 * siempre que lo necesiten, aunque no resuelve el problema de ser
 * justo, pues un mismo writer puede adquirir indefinidamente el writeLock
 * sin que otros writers puedan realizar su tarea. Por otro lado
 * los readers pueden sufrir de hambruna.
 */
public class WritersWithPreferenceReadWriteLock implements ReadWriteLock {
  private Lock readLock;
  private Lock writeLock;
  private boolean writer;
  private int readers;
  private Lock lock;
  private Condition condition;

  public WritersWithPreferenceReadWriteLock() {
    this.writer = false;
    this.readers = 0;
    this.lock = new ReentrantLock();
    this.condition = this.lock.newCondition();
    this.readLock = new ReadLock();
    this.writeLock = new WriteLock();
  }

  @Override
  public Lock readLock() {
    return readLock;
  }

  @Override
  public Lock writeLock() {
    return writeLock;
  }

  class ReadLock implements Lock {
    @Override
    public void lock() {
      lock.lock();
      try {
        while (writer) {
          condition.await();
        }
        readers++;
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }

    @Override
    public void unlock() {
      lock.lock();
      try {
        readers--;
        if (readers == 0) {
          condition.signalAll();
        }
      } finally {
        lock.unlock();
      }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {}

    @Override
    public boolean tryLock() {
      return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
      return false;
    }

    @Override
    public Condition newCondition() {
      return null;
    }
  }

  class WriteLock implements Lock {

    @Override
    public void lock() {
      lock.lock();
      try {
        while (writer) {
          condition.await();
        }
        writer = true;
        while (readers > 0) {
          condition.await();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }

    @Override
    public void unlock() {
      lock.lock();
      try {
        writer = false;
        condition.signalAll();
      } finally {
        lock.unlock();
      }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {}

    @Override
    public boolean tryLock() {
      return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
      return false;
    }

    @Override
    public Condition newCondition() {
      return null;
    }
  }
}
