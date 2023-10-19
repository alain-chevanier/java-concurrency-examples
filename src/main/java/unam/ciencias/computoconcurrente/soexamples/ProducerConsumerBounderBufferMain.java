package unam.ciencias.computoconcurrente.soexamples;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ProducerConsumerBounderBufferMain {
  static Buffer<Integer> buffer = new ProducerConsumerBoundedBuffer<>(100);
  static final int VALUES_TO_BE_CONSUMED = 1000;

  static Map<Integer, List<Integer>> producedElements = new Hashtable<>();
  static Map<Integer,List<Integer>> consumedElements = new Hashtable<>();

  static final int TIMEOUT_MS = 4000;

  static final int PRODUCERS = 1;
  static final int CONSUMERS = 1;

  static List<Thread> producersThreads = new ArrayList<>(PRODUCERS);
  static List<Thread> consumersThreads = new ArrayList<>(CONSUMERS);
  static List<Thread> allThreads = new ArrayList<>(PRODUCERS + CONSUMERS);

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < PRODUCERS; i++) {
      int elems = VALUES_TO_BE_CONSUMED * CONSUMERS / PRODUCERS;
      if (i == 0) {
        elems += (VALUES_TO_BE_CONSUMED * CONSUMERS) % PRODUCERS;
      }
      final int elementsToProduce = elems;
      Thread producer = new Thread(() -> produceElements(elementsToProduce), "Producer" + (i+1));
      producersThreads.add(producer);
      allThreads.add(producer);
    }

    for (int i = 0; i < CONSUMERS; i++) {
      Thread consumer = new Thread(() -> consumeElements(), "Consumer" + (i+1));
      consumersThreads.add(consumer);
      allThreads.add(consumer);
    }

    for (var thread : allThreads) {
      thread.start();
    }

    int waits = 0;
    while(allThreads.stream().anyMatch(Thread::isAlive) && waits < (CONSUMERS + PRODUCERS)) {
      Thread.sleep(TIMEOUT_MS);
      waits++;
    }

    for (var thread : allThreads) {
      if (thread.isAlive()) {
        thread.interrupt();
      }
    }

    List<Integer> allProducedElems = new ArrayList<>();
    for (var pair : producedElements.entrySet()) {
      allProducedElems.addAll(pair.getValue());
    }

    List<Integer> allConsumedElems = new ArrayList<>();
    for (var pair : consumedElements.entrySet()) {
      allConsumedElems.addAll(pair.getValue());
    }

    System.out.println("the same amount of elements was produced and consumed? "
      + (allProducedElems.size() == allConsumedElems.size()));

    System.out.println("Produced elems: " + allProducedElems.size()
      + ". Consumed elems: " + allConsumedElems.size());

    System.out.println("Any consumed elements is null? " + allConsumedElems.stream().anyMatch(Objects::isNull));

    allProducedElems.sort(Integer::compareTo);
    allConsumedElems.sort(Integer::compareTo);

    System.out.println("produced and consumed elements are the same? "
      + (allProducedElems.equals(allConsumedElems)));
  }

  static void produceElements(int elementsToProduce) {
    try {
      produceElementsAux(elementsToProduce);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static void produceElementsAux(int elementsToProduce) throws InterruptedException {
    List<Integer> producedElems = new ArrayList<>();
    producedElements.put(ThreadID.get(), producedElems);
    for (int i = 0; i < elementsToProduce; i++) {
      Integer elem = ThreadLocalRandom.current().nextInt();
      buffer.put(elem);
      // sleep some random time before producing next element
      producedElems.add(elem);
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
    List<Integer> consumedElems = new ArrayList<>();
    consumedElements.put(ThreadID.get(), consumedElems);
    for (int i = 0; i < VALUES_TO_BE_CONSUMED; i++) {
      Integer elem = buffer.take();
      consumedElems.add(elem);
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
