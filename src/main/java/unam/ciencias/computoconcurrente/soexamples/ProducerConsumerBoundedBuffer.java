package unam.ciencias.computoconcurrente.soexamples;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class ProducerConsumerBoundedBuffer<T> implements Buffer<T> {
  public static final int DEFAULT_SIZE = 20;

  private final int size;
  private final T[] buffer;
  private int elements;
  private int nextPutIndex;
  private int nextTakeIndex;

  private Semaphore availableElement;
  private Semaphore availableSpot;
  private Lock mutex;

  public ProducerConsumerBoundedBuffer() {
    this(DEFAULT_SIZE);
  }

  public ProducerConsumerBoundedBuffer(int size) {
    this.size = size;
    this.elements = 0;
    this.nextPutIndex = 0;
    this.nextTakeIndex = 0;
    this.buffer = (T[]) new Object[size];
  }

  public int capacity() {
    return this.size;
  }

  public void put(T item) throws InterruptedException {
    while (this.isFull()) {
      // wait till there's room to produce something
      if (Thread.interrupted()) {
        // abort in case we are said to be interrupted
        throw new InterruptedException();
      }
      Thread.yield();
    }
    this.buffer[this.nextPutIndex] = item;
    this.nextPutIndex = (this.nextPutIndex + 1) % this.size;
    this.elements++;
  }

  public T take() throws InterruptedException {
    while (this.isEmpty()) {
      // wait till there is something to consume
      if (Thread.interrupted()) {
        // abort in case we are said to be interrupted
        throw new InterruptedException();
      }
      Thread.yield();
    }
    T value;
    this.elements--;
    value = this.buffer[this.nextTakeIndex];
    this.nextTakeIndex = (this.nextTakeIndex + 1) % size;

    return value;
  }

  public int elements() {
    return this.elements;
  }
}