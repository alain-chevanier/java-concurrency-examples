package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ExplicitLockAndConditionsBoundedBoundedBuffer<T> implements BoundedBuffer<T> {

  public static final int DEFAULT_SIZE = 20;

  final Lock lock;
  final Condition notFull;
  final Condition notEmpty;

  final T[] items;

  int putIndex;
  int takeIndex;
  int elements;

  public ExplicitLockAndConditionsBoundedBoundedBuffer() {
    this(DEFAULT_SIZE);
  }

  public ExplicitLockAndConditionsBoundedBoundedBuffer(int size) {
    this.items = (T[]) new Object[size];
    this.lock = new ReentrantLock();
    this.notFull = lock.newCondition();
    this.notEmpty = lock.newCondition();
    putIndex = takeIndex = elements = 0;
  }

  @Override
  public void put(T x) throws InterruptedException {
    lock.lock();
    try {
      // espera mientras el buffer esté lleno
      while (this.isFull()) {
        notFull.await(); // cede el lock y el procesador
      }
      items[putIndex] = x;
      putIndex = (putIndex + 1) % capacity();
      ++elements;
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public T take() throws InterruptedException {
    lock.lock();
    try {
      // espera mientras el buffer está vacío
      while (this.isEmpty()) {
        notEmpty.await(); // cede el lock y el procesador
      }

      T x = items[takeIndex];
      takeIndex = (takeIndex + 1) % capacity();
      --elements;
      notFull.signal();
      return x;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int capacity() {
    return this.items.length;
  }

  @Override
  public int elements() {
    return this.elements;
  }
}
