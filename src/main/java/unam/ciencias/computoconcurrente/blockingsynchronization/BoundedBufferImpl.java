package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBufferImpl<T>
  implements BoundedBuffer<T> {

  private T[] buffer;
  private int elements;
  private int putIndex;
  private int takeIndex;

  private Lock lock;
  private Condition condition;

  public BoundedBufferImpl(int capacity) {
    this.buffer = (T[]) new Object[capacity];
    this.elements = 0;
    this.putIndex = 0;
    this.takeIndex = 0;
    this.lock = new ReentrantLock();
    this.condition = this.lock.newCondition();
  }

  @Override
  public int capacity() {
    return this.buffer.length;
  }

  @Override
  public int elements() {
    return this.elements;
  }

  @Override
  public void put(T item) throws InterruptedException {
    this.lock.lock();
    try {
      while (this.isFull()) {
        // busy - wait ESTO NO FUNCIONA
        // libero el candado
        //this.lock.unlock();
        // // me espero tantito
        //Thread.sleep(10);
        //Thread.yield();
        // // vuelvo a adquirir el candado
        //this.lock.lock();
        this.condition.await(10, TimeUnit.MILLISECONDS);
        //this.condition.await();
      }
      this.buffer[this.putIndex] = item;
      this.putIndex = (this.putIndex + 1) % this.capacity();
      this.elements++;
      //this.condition.signalAll();
      this.condition.signal();
    } finally {
      this.lock.unlock();
    }
  }

  @Override
  public T take() throws InterruptedException {
    this.lock.lock();
    try {
      while (this.isEmpty()) {
        // busy - wait
        // libero el candado
        //this.lock.unlock();
        // me espero tantito
        // Thread.sleep(10);
        //Thread.yield();
        // // vuelvo a adquirir el candado
        // this.lock.lock();
        this.condition.await(10, TimeUnit.MILLISECONDS); // equivalente a Thread.sleep(10)
        // this.condition.await();
      }
      T item = this.buffer[this.takeIndex];
      this.takeIndex = (this.takeIndex + 1) % this.capacity();
      this.elements--;
      // this.condition.signalAll();
      this.condition.signal();
      return item;
    } finally {
      this.lock.unlock();
    }
  }
}
