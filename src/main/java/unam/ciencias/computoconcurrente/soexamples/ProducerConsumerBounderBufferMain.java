package unam.ciencias.computoconcurrente.soexamples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProducerConsumerBounderBufferMain {
  static Buffer<Integer> buffer = new ProducerConsumerBoundedBuffer(100);
  static final int VALUES_TO_PRODUCE = 100;

  static List<Integer> producedElements = new ArrayList<>(VALUES_TO_PRODUCE);
  static List<Integer> consumedElements = new ArrayList<>(VALUES_TO_PRODUCE);


  public static void main(String[] args) throws InterruptedException {
    Thread producer = new Thread(() -> produceElements(), "Producer");
    Thread consumer = new Thread(() -> consumeElements(), "Consumer");

    producer.start();
    consumer.start();

    producer.join();
    consumer.join();

    System.out.println("produced and consumed elements are the same? "
      + producedElements.equals(consumedElements));

    for (int i = 0; i < VALUES_TO_PRODUCE; i++) {
      if (!producedElements.get(i).equals(consumedElements.get(i))) {
        System.out.println("elements at position " + i + " are different. "
        + "producedElem: "+ producedElements.get(i)
          + ", consumedElem: " + consumedElements.get(i));
      }
    }
  }

  static void produceElements() {
    try {
      produceElementsAux();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static void produceElementsAux() throws InterruptedException {

    for (int i = 0; i < VALUES_TO_PRODUCE; i++) {
      Integer elem = ThreadLocalRandom.current().nextInt();
      buffer.put(elem);
      // sleep some random time before producing next element
      producedElements.add(elem);
      sleepRandomTime();
    }
    System.out.println(Thread.currentThread().getName() + " is done. ");
  }

  static void consumeElements() {
    try {
      consumeElementsAux();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static void consumeElementsAux() throws InterruptedException {
    for (int i = 0; i < VALUES_TO_PRODUCE; i++) {
      Integer elem = buffer.take();
      consumedElements.add(elem);
      // sleep some random time before consuming next element
      sleepRandomTime();
    }
    System.out.println(Thread.currentThread().getName() + " is done.");
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
