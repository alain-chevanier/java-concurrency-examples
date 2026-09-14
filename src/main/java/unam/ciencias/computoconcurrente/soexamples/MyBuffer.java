package unam.ciencias.computoconcurrente.soexamples;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

public class MyBuffer<T> implements Buffer<T> {
    private final T[] buffer;
    private int nextPut;
    private int nextTake;
    private final int capacity;
    private int elements;
    private Lock mutex;
    private Condition notFull;
    private Condition notEmpty;

    @SuppressWarnings("unchecked")
    public MyBuffer(int capacity) {
        this.capacity = capacity;
        this.elements = 0;
        this.buffer = (T[]) new Object[capacity];
        this.nextPut = 0;
        this.nextTake = 0;
        this.mutex = new ReentrantLock();
        this.notFull = this.mutex.newCondition();
        this.notEmpty = this.mutex.newCondition();
    }

    /**
   * Regresa el tamaño del buffer.
   *
   * @return size
   */
  public int capacity() {
    return this.capacity;
  }

  /**
   * Agrega un elemento al buffer se bloquea si esta lleno
   *
   * @param item
   */
  public void put(T item) throws InterruptedException {
    this.mutex.lock(); // acquire

    try {
        while (this.isFull()) {
            /* this.mutex.unlock();
            Thread.sleep(50);
            this.mutex.lock(); */
            this.notFull.await();
        }
        this.buffer[nextPut++] = item;
        this.elements++;
        this.nextPut = this.nextPut % this.capacity;
        this.notEmpty.signalAll();
    } finally {
        this.mutex.unlock(); // release
    }
  }

  /**
   * Elimina un elemento del buffer; se bloquea si esta vacío.
   *
   * @return siguienteElemento
   */
  public T take() throws InterruptedException {
    this.mutex.lock(); // acquire
    try {
        while (this.isEmpty()) {
            /* this.mutex.unlock();
            Thread.sleep(50);
            this.mutex.lock(); */
            this.notEmpty.await();
        }
        T item = this.buffer[nextTake++];
        this.elements--;
        this.nextTake = this.nextTake % this.capacity;
        this.notFull.signalAll();
        return item;
    } finally {
        this.mutex.unlock(); // release
    }
  }

  /**
   * Obtiene el número de elementos del buffer.
   *
   * @return contedoDeObjetos
   */
  public int elements() {
    this.mutex.lock();
    try {
        return this.elements;    
    } finally {
        this.mutex.unlock(); // release
    }
  }
	
}
