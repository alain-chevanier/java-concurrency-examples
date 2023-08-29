package unam.ciencias.computoconcurrente.threadobjects;

import java.util.Arrays;

public class App {

  public static void main(String[] a) throws InterruptedException {
    Thread runnable = new Thread(new HelloRunnable());
    runnable.start();

    Thread inheritThread = new HelloThread();
    inheritThread.start();

    Thread anonymousClassThread = new Thread(
      () -> System.out.printf("%s: Hello from anonymous class\n", Thread.currentThread().getName())
    );
//        new Thread(
//            new Runnable() {
//              @Override
//              public void run() {
//                System.out.printf("%s: Hello from anonymous class\n", Thread.currentThread().getName());
//              }
//            });

    anonymousClassThread.start();

    Thread lambdaThread = new Thread(App::sleepingRoutine);
    // new Thread(() -> sleepingRoutine());
    lambdaThread.start();

    Thread.sleep(5000);
    System.out.printf("%s: Main thread will finish\n", Thread.currentThread().getName());

    SimpleThreads.example(a);
  }

  static void sleepingRoutine() {
    String importantInfo[] = {
      "Mares eat oats", "Does eat oats", "Little lambs eat ivy", "A kid will eat ivy too"
    };

//    Arrays.stream(importantInfo).forEach(s -> System.out.println(s));
//
//    for(String s : importantInfo) {
//    ...
//    }

    for (int i = 0; i < importantInfo.length; i++) {
      // Pause for 4 seconds
      try {
        Thread.sleep(4000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      // Print a message
      System.out.printf("%s -> CHILD THREAD: %s\n",
        Thread.currentThread().getName(),
        importantInfo[i]);
    }
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
