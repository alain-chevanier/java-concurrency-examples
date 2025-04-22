package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class MyBoundedBufferS implements BoundedBuffer<String> {

    private int capacity;
    private int elements;
    private String[] buffer;
    private int putIndex;
    private int takeIndex;

    private Semaphore availableSpots;
    private Semaphore availableItems;
    private Lock lock;

    public MyBoundedBufferS(int size) {
        this.capacity = size;
        this.elements = 0;
        this.buffer = new String[size];
        this.putIndex = 0;
        this.takeIndex = 0;
        this.lock = new ReentrantLock();
        this.availableSpots = new Semaphore(size);
        this.availableItems = new Semaphore(0);
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public int elements() {
        return this.elements;
    }

    @Override
    public void put(String item) throws InterruptedException {
        availableSpots.acquire(); // Espera a que haya un espacio disponible

        lock.lock();
        try {
            buffer[putIndex] = item;
            putIndex = (putIndex + 1) % capacity;
            elements++;
            availableItems.release(); // Notifica que hay un nuevo elemento disponible
        } finally {
            lock.unlock();
        }
        
    }

    @Override
    public String take() throws InterruptedException {
        availableItems.acquire(); // Espera a que haya un elemento disponible
        lock.lock();
        try {
            String item = buffer[takeIndex];
            takeIndex = (takeIndex + 1) % capacity;
            elements--;
            availableSpots.release(); // Notifica que hay un espacio disponible
            return item;
        } finally {
            lock.unlock();
        }
    }
    
}
