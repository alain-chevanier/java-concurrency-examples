package unam.ciencias.computoconcurrente.blockingsynchronization;

import java.util.concurrent.locks.Lock;

/**
 * Un ReadWrite Lock tiene las siguientes propiedades:
 * - multiples readers pueden adquirir el ReadLock simultáneamente
 *   siempre y cuando ningún writer haya adquirido el writeLock,
 *   en caso contrario tienen que esperar hasta que los writers terminen.
 * - solamente un writer puede adquirir el writeLock al mismo tiempo.
 *   Si hay readers que adquirieron el readLock, el writer debe esperar
 *   a que terminen para poder adquirir el writeLock.
 */
public interface ReadWriteLock {
  Lock readLock();
  Lock writeLock();
}
