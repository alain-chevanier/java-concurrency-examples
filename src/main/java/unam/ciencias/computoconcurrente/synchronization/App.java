package unam.ciencias.computoconcurrente.synchronization;

public class App {
  static SynchronizedCounter counter;

  public static void main(String[] a) throws InterruptedException {
    counter = new SynchronizedCounter();
    Long currentTimestamp = System.currentTimeMillis();
    Thread t1 = new Thread(() -> incrementCounterALotOfTimes(10000000, currentTimestamp + 3000));
    Thread t2 = new Thread(() -> incrementCounterALotOfTimes(10000000, currentTimestamp + 3000));

    Thread reporter = new Thread(() -> reportThreadStatus(t1, t2));
    reporter.setDaemon(true);
    reporter.start();

    t1.start();
    t2.start();

    t1.join();
    t2.join();

    System.out.println("Main thread current counter last value: " + counter.value());
  }

  static void incrementCounterALotOfTimes(int times, long startTimestamp) {
    // Magia negra: Estoy forzando a que ambos hilos inicies su ejecución al mismo tiempo
    try {
      Long currentTimestamp = System.currentTimeMillis();
      Long wakeUpTimestamp = startTimestamp - currentTimestamp;
      if (wakeUpTimestamp > 0) {
        Thread.sleep(wakeUpTimestamp);
      }
    } catch (InterruptedException ie) {
      System.out.println("me interrumpieron");
    }

    for (int i = 0; i < times; i++) {
      counter.increment();
    }
  }

  static void reportThreadStatus(Thread t1, Thread t2) {
    try {
      while (true) {
        Thread.sleep(500);
        System.out.println("t1: " + t1.getState() + "\n" + "t2: " + t2.getState());
      }
    } catch (InterruptedException ie) {
      System.out.println("ya me morí");
    }
  }

  static void exampleHappensBeforeStartAndJoin() throws InterruptedException {
    counter = new SynchronizedCounter();
    Thread t = new Thread(() -> incrementCounterALotOfTimes());
    counter.increment();
    counter.increment();
    counter.decrement();
    t.start();
    for (int j = 0; j < 10; j++) {
      Thread.sleep(1000);
      System.out.println("Main thread current counter value: " + counter.value());
    }
    t.join();
    System.out.println("Main thread current counter last value: " + counter.value());
  }

  static void incrementCounterALotOfTimes() {
    try {
      for (int i = 0; i < 1000; i++) {
        Thread.sleep(10);
        counter.increment();
      }
      System.out.println("Child Thread: Current Counter Value: " + counter.value());
    } catch (InterruptedException ie) {
      System.out.println("me interrumpieron, terminando");
    }
  }
}
