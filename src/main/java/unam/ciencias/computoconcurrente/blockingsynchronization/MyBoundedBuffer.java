package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyBoundedBuffer<T> implements BoundedBuffer<T> {
    int capacity;
    int elements;
    T[] buffer;
    int putIndex;
    int takeIndex;
    Lock lock;
    Condition notFull;
    Condition notEmpty;

    public MyBoundedBuffer(int size) {
        this.capacity = size;
        this.elements = 0;
        this.buffer = (T[]) new Object[size];
        this.putIndex = 0;
        this.takeIndex = 0;
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public int elements() {
        // Implementación del método elements
        return this.elements;
    }

    @Override
    public void put(T item) throws InterruptedException {
        // Implementación del método put
        lock.lock();
        try {
            while (isFull()) {
                // me tengo que quedar esperando a que se libere un espacio
                /*
                lock.unlock();
                Thread.sleep(100); // Simulación de espera
                lock.lock();
                */
                notFull.await();
            }

            buffer[putIndex] = item;
            putIndex = (putIndex + 1) % capacity;
            elements++;

            notEmpty.signalAll(); // Notificar a los consumidores que hay un elemento disponible
        } finally {
            lock.unlock();
        } 
    }
    
    @Override 
    public T take() throws InterruptedException {
        // Implementación del método take
        lock.lock();
        try {
            while (isEmpty()) {
                // me tengo que quedar esperando a que se libere un espacio
                /* lock.unlock();
                Thread.sleep(100); // Simulación de espera
                lock.lock(); */
                notEmpty.await();
            }
            T item = buffer[takeIndex];
            takeIndex = (takeIndex + 1) % capacity;
            elements--;
            notFull.signalAll(); // Notificar a los productores que hay espacio disponible
            return item;
        } finally {
            lock.unlock();
        }
    }

    
}
