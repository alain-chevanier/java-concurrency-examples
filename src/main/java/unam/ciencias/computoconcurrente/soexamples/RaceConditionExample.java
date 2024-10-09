package unam.ciencias.computoconcurrente.soexamples;

/**
 * En esta clase tenemos un ejemplo de cómo sucede una condición de carrera.
 */
public class RaceConditionExample {

  // variable global que comparten todos los hilos
  static int counter = 0;

  static final Lock lock = new PetersonLock();

  static final int MAX_VALUE = 10000000;

  public static void main(String[] a) throws InterruptedException {
    System.out.println("Ejemplo de una condición de carrera (race condition)");

    // el hilo t1 ejecuta el método "doWork", que hace counter++ 10k veces
    Thread t1 = new Thread(() -> doWork());
    // el hilo t2 ejecuta el método "undoWork", que hace counter-- 10k veces
    Thread t2 = new Thread(() -> undoWork());

    // aquí arrancamos ambos hilos
    t2.start();
    t1.start();

    // esperamos a que terminen
    t1.join();
    t2.join();

    // Al finalizar el trabajo de t1 y t2 imprimimos el valor del contador
    // debería de ser 0, pero no lo es, de hecho casi siempre produce un valor
    // diferente, a este fenómeno se le conoce como condición de carrera.
    // Revisar slides para visualizar cómo sucede este error.
    System.out.println("reached counter value: " + counter);
  }

  static void doWork() {
    // Critical Section Begin
    lock.lock();
    for (int i = 0; i < MAX_VALUE; i++) {
      counter++;
    }
    lock.unlock();
    // Critical Section End
  }

  static void undoWork() {
    // Critical Section Begin
    lock.lock();
    for (int i = 0; i < MAX_VALUE; i++) {
      counter--;
    }
    lock.unlock();
    // Critical Section End
  }
}
