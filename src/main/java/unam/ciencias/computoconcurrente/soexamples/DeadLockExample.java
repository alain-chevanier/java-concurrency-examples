package unam.ciencias.computoconcurrente.soexamples;


import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class DeadLockExample {
  static Semaphore S = new Semaphore(1);
  static Semaphore Q = new Semaphore(1);
  public static void main(String[] args) throws InterruptedException {
    System.out.println("Example of a deadlock using 2 threads and 2 semaphores");

    Thread producer = new Thread(() -> p0(), "P_0");
    Thread consumer = new Thread(() -> p1(), "P_1");

    producer.start();
    consumer.start();

    System.out.println("Waiting for process p_0 and p_1 to complete");

    producer.join();
    consumer.join();
//
    System.out.println("Execution finished ");

  }

  static void p0() {
    try {
      S.acquire();
      System.out.printf("%s acquired semaphore S\n", Thread.currentThread().getName());
      Thread.sleep(100);
      Q.acquire();
      System.out.printf("%s acquired semaphore Q\n", Thread.currentThread().getName());
      S.release();
      Q.release();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static void p1() {
    try {
      Q.acquire();
      System.out.printf("%s acquired semaphore Q\n", Thread.currentThread().getName());
      Thread.sleep(100);
      S.acquire();
      System.out.printf("%s acquired semaphore S\n", Thread.currentThread().getName());
      Q.release();
      S.release();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static void sleepRandomTime() {
    try {
      if (!Thread.currentThread().getName().equals("main")) {
        Thread.sleep(Math.abs(ThreadLocalRandom.current().nextInt() % 10));
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
