package unam.ciencias.computoconcurrente.threadobjects;

import java.util.Arrays;

public class App {

  public static void main(String[] a) throws InterruptedException {
    Thread runnable = new Thread(new HelloRunnable());
    runnable.start();

    Thread inheritThread = new HelloThread();
    inheritThread.start();

    Thread anonymousClassThread = new Thread(
      () -> System.out.printf("%s: Hello from anonymous class\n",
                              Thread.currentThread().getName())
    );
    anonymousClassThread.start();

    Thread methodReferenceThread = new Thread(App::sleepingRoutine);
    // is the same as ~new Thread(() -> sleepingRoutine())~
    methodReferenceThread.start();

    Thread.sleep(5000);
    System.out.printf("%s: Main thread will finish\n",
                      Thread.currentThread().getName());
  }

  static void sleepingRoutine() {
    String[] importantInfo = {
      "Mares eat oats",
      "Does eat oats",
      "Little lambs eat ivy",
      "A kid will eat ivy too"
    };

    Arrays.stream(importantInfo)
      .forEach((str) -> {
          try {
            Thread.sleep(4000);
            System.out.printf("%s -> CHILD THREAD: %s\n",
                              Thread.currentThread().getName(),
                              str);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
  }
}

class HelloRunnable implements Runnable {
  @Override
  public void run() {
    System.out.printf(
      "%s -> Hello from a thread within runnable interface!\n",
      Thread.currentThread().getName()
    );
  }
}

class HelloThread extends Thread {
  @Override
  public void run() {
    System.out.printf(
      "%s -> Hello from a thread from class that extends Thread!\n",
      Thread.currentThread().getName()
    );
  }
}
