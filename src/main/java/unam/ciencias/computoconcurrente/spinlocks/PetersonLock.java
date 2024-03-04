package unam.ciencias.computoconcurrente.spinlocks;

import java.util.List;

/**
 * This particular implementation of a lock uses the Peterson's Algorithm which only work for two
 * concurrent threads at most.
 */
public class PetersonLock implements Lock {

  private boolean[] flags;
  private volatile int lastToArrive;

  public PetersonLock() {
    flags = new boolean[] {false, false};
  }

  @Override
  public void lock() { // acquire
    int threadId = ThreadID.get(); // Obtengo mi ID: 0, 1
    // al llegar anuncio que quiero contender por entrar a la
    // sección crítica levantando mi bandera
    flags[threadId] = true;
    // luego anuncio que yo fui el último en llegar
    lastToArrive = threadId;
    // me quedo girando si el otro hilo también levantó su bandera y llegó primero
    // y me detengo cuando el otro hilo termine de ejecutar el código de la sección crítica
    while (hasTheOtherThreadRaisedItsFlag(threadId) && amITheLastToArrive(threadId)) {
      // no hace nada, solo me quedo girando hasta que el otro hilo termine de
      // ejecutar la sección crítica
    }
  }

  private boolean hasTheOtherThreadRaisedItsFlag(int myThreadId) {
    int theOtherThreadId = 1 - myThreadId;
    return flags[theOtherThreadId];
  }

  private boolean amITheLastToArrive(int myThreadId) {
    return lastToArrive == myThreadId;
  }

  @Override
  public void unlock() {
    int threadId = ThreadID.get();
    // para anunciar que ya terminé de ejecutar la sección crítica simplemente bajo mi bandera
    flags[threadId] = false;
  }
}
