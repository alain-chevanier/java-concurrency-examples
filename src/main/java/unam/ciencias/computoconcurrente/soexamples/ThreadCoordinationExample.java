package unam.ciencias.computoconcurrente.soexamples;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadCoordinationExample {
  // counts the numbers of produced elements
  static final int VALUES_TO_PRODUCE = 1000;
  static int[] values = new int[VALUES_TO_PRODUCE];

  public static void main(String[] args) throws InterruptedException {
    Semaphore synch = new Semaphore(0);
    Thread producer = new Thread(() -> produceValues(synch), "Producer");
    Thread consumer = new Thread(() -> countOddNumbers(synch), "Consumer");

    producer.start();
    consumer.start();

    producer.join();
    consumer.join();

    System.out.print("Actual produced odd numbers count: ");
    synch.release(VALUES_TO_PRODUCE);
    countOddNumbers(synch);
  }

  static void produceValues(Semaphore synch) {
    for (int i = 0; i < VALUES_TO_PRODUCE; i++) {
      values[i] = ThreadLocalRandom.current().nextInt();
      // notify there's a new element that can be consumed
      synch.release(); // synch -> 20
      // sleep some random time before next elem is produced
      sleepRandomTime();
    }
    System.out.println(Thread.currentThread().getName() + " is done. ");
  }

  static void countOddNumbers(Semaphore synch) {
    try {
      countOddNumbersAux(synch);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static void countOddNumbersAux(Semaphore synch) throws InterruptedException {
    int oddNumCount = 0;
    for (int i = 0; i < values.length; i++) {
      // wait till there's an element to consume
      synch.acquire(); // if synch <=0 wait (acquire/sema_down synch--)
      int value = values[i];
      oddNumCount += value % 2 == 1 ? 1 : 0;
      // sleep some random time before analysing next element
      sleepRandomTime();
    }
    System.out.println(Thread.currentThread().getName() +
      " found " + oddNumCount + " odd numbers");
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
