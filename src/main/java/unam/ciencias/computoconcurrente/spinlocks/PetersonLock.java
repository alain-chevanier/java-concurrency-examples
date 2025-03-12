package unam.ciencias.computoconcurrente.spinlocks;

/**
 * This particular implementation of a lock uses the Peterson's Algorithm which only work for two
 * concurrent threads at most.
 */
public class PetersonLock implements Lock {

  private VolatileBoolean[] flags;
  private volatile int victim;

  public PetersonLock() {
    flags = new VolatileBoolean[] {VolatileBoolean.of(false), VolatileBoolean.of(false)};
  }

  @Override
  public void lock() { // acquire
    int threadId = ThreadID.get(); // Obtengo mi ID: 0, 1
    // al llegar anuncio que quiero contender por entrar a la
    // sección crítica levantando mi bandera
    flags[threadId].setValue(true);
    // luego anuncio que yo fui el último en llegar
    victim = threadId;
    // me quedo girando si el otro hilo también levantó su bandera y llegó primero
    // y me detengo cuando el otro hilo termine de ejecutar el código de la sección crítica
    while (flags[1 - threadId].isValue() && victim == threadId) {
      // no hace nada, solo me quedo girando hasta que el otro hilo termine de
      // ejecutar la sección crítica
      // Thread.yield();
    }
  }

  @Override
  public void unlock() {
    int threadId = ThreadID.get();
    // para anunciar que ya terminé de ejecutar la sección crítica simplemente bajo mi bandera
    flags[threadId].setValue(false);
  }
}
