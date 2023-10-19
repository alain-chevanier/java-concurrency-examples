package unam.ciencias.computoconcurrente.soexamples;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadCoordinationExample {
  static Semaphore synch = new Semaphore(0);
  static final int VALUES_TO_PRODUCE = 1000;
  static int[] values = new int[VALUES_TO_PRODUCE];

  public static void main(String[] args) throws InterruptedException {
    Thread producer = new Thread(() -> produceValues(), "Producer");
    Thread consumer = new Thread(() -> countOddNumbers(), "Consumer");

    producer.start();
    consumer.start();

    producer.join();
    consumer.join();

    System.out.print("Actual produced odd numbers count: ");
    countOddNumbers();
  }

  static void produceValues() {
    for (int i = 0; i < VALUES_TO_PRODUCE; i++) {
      values[i] = ThreadLocalRandom.current().nextInt();
      // sleep some random time before next elem is produced
      sleepRandomTime();
    }
    System.out.println(Thread.currentThread().getName() + " is done. ");
  }

  static void countOddNumbers() {
    try {
      countOddNumbersAux();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static void countOddNumbersAux() throws InterruptedException {
    int oddNumCount = 0;
    for (int value : values) {
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
