package unam.ciencias.computoconcurrente.soexamples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

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
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // wait till there's an available spot
      Thread.yield();
    }
    this.buffer[this.nextPutIndex] = item;
    this.nextPutIndex = (this.nextPutIndex + 1) % this.size;
    this.elements++;
  }

  public T take() throws InterruptedException {
    while (this.isEmpty()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // wait till there is something to consume
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
