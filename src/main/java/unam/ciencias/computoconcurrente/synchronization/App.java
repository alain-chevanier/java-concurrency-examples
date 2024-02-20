package unam.ciencias.computoconcurrente.synchronization;

import java.util.List;
import java.util.ArrayList;

public class App {
  static Counter counter;

  public static void main(String[] a) throws InterruptedException {
    // Example of race condition + data race an solution
    // happens before without using volatile
    counterExample();

    // Example of how to establish a "happends-before" relationship using thread.start() and thread.join()
    // exampleHappensBeforeStartAndJoin();

    // Example of a child thread ending after the main thread
    // childThreadEndsLast();
  }

  static void counterExample() throws InterruptedException {
    // With race condition and data race
    counter = new ThreadUnsafeCounter();
    // No race condition or data race
    // // coment prev initialization and uncomment next line to see the fix
    // counter = new SynchronizedCounter();

    Long currentTimestamp = System.currentTimeMillis();
    Thread t1 = new Thread(() -> incrementCounterALotOfTimes(10000000, currentTimestamp + 3000));
    Thread t2 = new Thread(() -> incrementCounterALotOfTimes(10000000, currentTimestamp + 3000));

    // Start a deamon thread which will print the status of every thread but himself
    enableReporterThread(List.of(Thread.currentThread(), t1, t2));

    t1.start();
    t2.start();

    t1.join();
    t2.join();

    System.out.println("Main thread current counter last value: " + counter.value());
  }

  static void incrementCounterALotOfTimes(int times, long startTimestamp) {
    // Magia negra: Estoy forzando a que ambos hilos inicies su ejecución al mismo tiempo
    sleepTillGivenTimestamp(startTimestamp);

    for (int i = 0; i < times; i++) {
      counter.increment();
    }
  }

  private static void sleepTillGivenTimestamp(long startTimestamp) {
    try {
      long currentTimestamp = System.currentTimeMillis();
      long wakeUpTimestamp = startTimestamp - currentTimestamp;
      if (wakeUpTimestamp > 0) {
        Thread.sleep(wakeUpTimestamp);
      }
    } catch (InterruptedException ie) {
      System.out.println("me interrumpieron");
    }
  }

  private static void enableReporterThread(List<Thread> threads) {
    Thread reporter = new Thread(() -> reportThreadStatus(threads));
    reporter.setName("Reporter");
    reporter.setDaemon(true);
    reporter.start();
  }

  static void reportThreadStatus(List<Thread> threads) {
    List<Thread> allThreads = new ArrayList<>(threads);
    allThreads.add(Thread.currentThread());
    try {
      while (true) {
        Thread.sleep(250);
        allThreads.forEach((t) -> {
            System.out.printf("%s: %s,\t", t.getName(), t.getState());
          });
        System.out.println("");
      }
    } catch (InterruptedException ie) {
      System.out.println("ya me morí");
    }
  }

  static void exampleHappensBeforeStartAndJoin() throws InterruptedException {
    counter = new ThreadUnsafeCounter();
    Thread t = new Thread(() -> incrementCounterALotOfTimes());
    counter.increment();
    counter.increment();
    counter.decrement();
    // thread.start() establishes a "happens-before" relationship
    // so all changes made to `counter` by `main` thread
    // are immediatelly visible to thread `t`
    t.start();
    for (int j = 0; j < 10; j++) {
      Thread.sleep(1000);
      System.out.println("Main thread current counter value: " + counter.value());
    }
    // thread.join() establishes a "happens-before" relationship
    // so all changes made to `counter` by thread `t`
    // are immediatelly visible to `main` thread
    t.join();
    // so this always prints the last value written to `counter` by thread `t`
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

  static void childThreadEndsLast() {
    Thread t = new Thread(App::foo);
    t.start();
    System.out.println("Main thread finishes");
  }

  static void foo() {
    try {
      Thread.sleep(3000);
      System.out.println("Child Thread done");
    } catch (InterruptedException e) {
      System.out.println("Error " + e.getMessage());
      e.printStackTrace();
    }
  }
}
