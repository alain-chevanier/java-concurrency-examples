package unam.ciencias.computoconcurrente.blockingsynchronization;

/**
 * Similar a Lock, pero en vez de permitir que solamente
 * acceda un thread a la sección crítica (exclusión mutua)
 * permite que entren hasta k >= 1 hilos a la sección
 * crítica al mismo tiempo.
 */
public interface Semaphore {
  void down(); // equivalente a "lock" - acquire de Lock

  void up(); // equivalente a "unlock" - release de Lock

  int permits(); // threads que pueden entrar a la CS al mismo tiempo

  ThreadID getThreadId();
}
