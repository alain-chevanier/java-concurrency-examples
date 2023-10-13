package unam.ciencias.computoconcurrente.blockingsynchronization;

/**
 * Buffer de tamaño acotado para representar del problema del productor
 * consumidor.
 */
public interface BoundedBuffer<T> {

  /**
   * Regresa el tamaño del buffer.
   *
   * @return size
   */
  int capacity();

  /**
   * Agrega un elemento al buffer.
   * Si el buffer está lleno el thread productor debe de esperar.
   *
   * @param item
   */
  void put(T item) throws InterruptedException;

  /**
   * Elimina un elemento del buffer.
   * Si el buffer está vacío el thread consumidor debe de esperar.
   *
   * @return siguienteElemento
   */
  T take() throws InterruptedException;

  /**
   * Obtiene el número de elementos del buffer.
   *
   * @return contedoDeObjetos
   */
  int elements();

  default boolean isFull() {
    return capacity() == elements();
  }

  default boolean isEmpty() {
    return elements() == 0;
  }
}
