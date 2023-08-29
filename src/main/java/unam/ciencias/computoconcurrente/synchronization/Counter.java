package unam.ciencias.computoconcurrente.synchronization;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
  volatile int c = 0;
  Lock lock = new ReentrantLock();
  Semaphore semaphore = new Semaphore(1);

  void increment() {
    // Primitivas de sincronización
    // locks -
    // semaphores
    // apagar interrupciones externas

    // sección crítica
    // CS: inicia aquí
    // Solución utilizando un lock
//    lock.lock(); // lock.acquire()
//    c++; // se traduce en 3 instrucciones al compilarse
//    lock.unlock();

    // Solución utilizando un semáforo binario (permits = 1)
//    try {
//      semaphore.acquire();
//      c++;
//      semaphore.release();
//    } catch (InterruptedException ie) {
//      System.out.println("me interrumpieron");
//    }

    // apaga las interrupciones de hardware
    // enum interrupt_level  prev_level = intr_disable ();
    c++;
    // hacemos más cosas
    // más instrucciones...
    // prende las interrupciones de hardware
    // intr_set_level (prev_level);

    // CS: termina aquí


  }

  void decrement() {
    c--;
  }

  int value() {
    return c;
  }
}
