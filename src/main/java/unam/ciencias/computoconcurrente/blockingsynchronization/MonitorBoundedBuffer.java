package unam.ciencias.computoconcurrente.blockingsynchronization;

/**
 * Implementación de un buffer concurrente (Productor/Consumidor)
 * Utilizando el lock intrínseco y la condición que este tiene asociada.
 */
public class MonitorBoundedBuffer<T> implements BoundedBuffer<T> {

  private int capacity;
  private int elements;
  private T[] buffer;
  private int takeIndex;
  private int putIndex;

  public MonitorBoundedBuffer() {
    this(20);
  }

  public MonitorBoundedBuffer(int capacity) {
    this.capacity = capacity;
    this.buffer = (T[]) new Object[capacity];
    this.elements = 0;
    this.putIndex = 0;
    this.takeIndex = 0;
  }

  @Override
  public int capacity() {
    return this.capacity;
  }

  @Override
  public synchronized void put(T item) throws InterruptedException {
    // espera mientras el buffer esté lleno
    while (this.isFull()) {
      wait(); // equivalente a cond.await()
    }
    buffer[putIndex] = item;
    putIndex = (putIndex + 1) % capacity;
    elements++;
    notifyAll(); // signalAll()
    // notify(); // signal()
  }

  @Override
  public synchronized T take() throws InterruptedException {
    // espera mientras el buffer esté vacío
    while (this.isEmpty()) {
      wait(); // equivalente a cond.await()
    }
    T elem = buffer[takeIndex];
    takeIndex = (takeIndex + 1) % capacity;
    elements--;
    notifyAll(); // equivalente a cond.signalAll()
    return elem;
  }

  @Override
  public int elements() {
    return this.elements;
  }
}
