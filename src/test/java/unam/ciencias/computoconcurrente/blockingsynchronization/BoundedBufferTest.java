package unam.ciencias.computoconcurrente.blockingsynchronization;

import org.junit.jupiter.api.condition.EnabledIf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static unam.ciencias.computoconcurrente.PropertiesLoader.getBooleanProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@EnabledIf("testSuiteEnabled")
class BoundedBufferTest {

  static boolean testSuiteEnabled() {
    return getBooleanProperty("bounded-buffer.enabled");
  }

  final int LOOPS = 100;
  final int MIN_SLEEP_TIME = 5;
  final int MAX_SLEEP_TIME = 10;
  final int MAX_TEST_EXECUTION_TIME = 10000;

  List<Thread> threads;
  BoundedBuffer<Integer> buffer;
  ConcurrentLinkedQueue<Integer> result;
  ConcurrentLinkedQueue<Integer> expectedResult;
  Random random;
  Integer consumers;
  Integer producers;

  Object lockPut;
  Object lockTake;

  static final int MAX_ALLOWED_THREADS = Math.min(4, Runtime.getRuntime().availableProcessors());

  @BeforeEach
  void setUp() {
    this.result = new ConcurrentLinkedQueue<>();
    this.expectedResult = new ConcurrentLinkedQueue<>();

    this.random = new Random();
    this.lockPut = new Object();
    this.lockTake = new Object();
  }

  @Test
  void testSingleThreadFIFO() throws Exception {
    System.out.printf("MAIN THREAD: Testing sequential behavior is correct.\n");
    this.buffer = new BoundedBufferImpl<>(LOOPS);
    this.producers = 1;
    this.consumers = 1;
    produce();
    consume();
    assertEquals(result.size(), LOOPS);
    assertEquals(expectedResult.size(), LOOPS);
    assertEquals(toList(expectedResult), toList(result));
  }

  @Test
  void testSingleProducerSingleConsumer() throws Exception {
    this.producers = 1;
    this.consumers = 1;
    runSimulation(this.producers, this.consumers);
    assertEquals(result.size(), LOOPS);
    assertEquals(expectedResult.size(), LOOPS);
    assertEquals(toList(expectedResult), toList(result));
  }

  @Test
  void testSingleProducerMultipleConsumers() throws InterruptedException {
    this.producers = 1;
    this.consumers = MAX_ALLOWED_THREADS;
    runSimulation(this.producers, this.consumers);
    assertEquals(result.size(), LOOPS);
    assertEquals(sort(toList(expectedResult)), sort(toList(result)));
  }

  @Test
  void testMultipleProducersSingleConsumer() throws InterruptedException {
    this.producers = MAX_ALLOWED_THREADS;
    this.consumers = 1;
    runSimulation(this.producers, this.consumers);
    assertEquals(result.size(), LOOPS);
    assertEquals(expectedResult.size(), LOOPS);
    assertEquals(sort(toList(expectedResult)), sort(toList(result)));
  }

  @Test
  void testMultipleProducersMultipleConsumersCaseOne() throws InterruptedException {
    this.producers = Math.max(1, MAX_ALLOWED_THREADS / 2);
    this.consumers = MAX_ALLOWED_THREADS;
    runSimulation(this.producers, this.consumers);
    assertEquals(result.size(), LOOPS);
    assertEquals(expectedResult.size(), LOOPS);
    assertEquals(sort(toList(expectedResult)), sort(toList(result)));
  }

  @Test
  void testMultipleProducersMultipleConsumersCaseTwo() throws InterruptedException {
    this.producers = MAX_ALLOWED_THREADS;
    this.consumers = Math.max(1, MAX_ALLOWED_THREADS / 2);
    runSimulation(this.producers, this.consumers);
    assertEquals(result.size(), LOOPS);
    assertEquals(expectedResult.size(), LOOPS);
    assertEquals(sort(toList(expectedResult)), sort(toList(result)));
  }

  @Test
  void testMultipleProducersMultipleConsumersCaseThree() throws InterruptedException {
    this.producers = MAX_ALLOWED_THREADS;
    this.consumers = MAX_ALLOWED_THREADS;
    runSimulation(this.producers, this.consumers);
    assertEquals(result.size(), LOOPS);
    assertEquals(expectedResult.size(), LOOPS);
    assertEquals(sort(toList(expectedResult)), sort(toList(result)));
  }

  void runSimulation(int producers, int consumers) throws InterruptedException {
    System.out.printf(
        "MAIN THREAD: Starting test case for %d producer(s), %d consumer(s).\n",
        producers, consumers);
    this.buffer = new BoundedBufferImpl<>(LOOPS);
    this.threads = new ArrayList<>();
    for (int i = 0; i < producers; i++) {
      threads.add(new Thread(this::produce));
    }
    for (int i = 0; i < consumers; i++) {
      threads.add(new Thread(this::consume));
    }
    for (Thread t : threads) {
      t.start();
    }
    long currentTime = System.currentTimeMillis();
    while (result.size() < LOOPS
        && areThereActiveThreads()
        && System.currentTimeMillis() - currentTime <= MAX_TEST_EXECUTION_TIME) {
      assertTrue(buffer.elements() <= buffer.capacity());
      sleepRandomTime();
    }
    if (result.size() < LOOPS) {
      System.out.println(
          "MAIN THREAD interrupting other threads, possible they didn't finish and you might have a deadlock");
      for (Thread t : threads) {
        t.interrupt();
      }
    }
    for (Thread t : threads) {
      t.join();
    }
  }

  void produce() {
    String threadName = Thread.currentThread().getName();
    System.out.printf("Producer %s starting execution\n", threadName);
    try {
      for (int i = 0; i < LOOPS / producers; i++) {
        Integer randomInt = random.nextInt();
        System.out.printf("it: %s. %s producing %s\n", i, threadName, randomInt);
        synchronized (lockPut) {
          buffer.put(randomInt);
          expectedResult.add(randomInt);
        }
        int rate = consumers / producers;
        int speed = rate == 0 ? 1 : rate;
        if (i % speed == 0) {
          sleepRandomTime();
        }
      }
    } catch (InterruptedException ignored) {
      System.out.printf("Producer %s didn't finish the work\n", threadName);
    } finally {
      System.out.printf("Producer %s finishing execution\n", threadName);
    }
  }

  void consume() {
    System.out.printf("Consumer %s starting execution\n", Thread.currentThread().getName());
    String threadName = Thread.currentThread().getName();
    try {
      for (int i = 0; i < LOOPS / consumers; i++) {
        System.out.printf("it: %s. %s consumig\n", i, threadName);
        synchronized (lockTake) {
          result.add(buffer.take());
        }
        int rate = producers / consumers;
        int speed = rate == 0 ? 1 : rate;
        if (i % speed == 0) {
          sleepRandomTime();
        }
      }
    } catch (InterruptedException ignored) {
      System.out.printf("Consumer %s didn't finish the work\n", Thread.currentThread().getName());
    } finally {
      System.out.printf("Consumer %s finishing execution\n", Thread.currentThread().getName());
    }
  }

  void sleepRandomTime() throws InterruptedException {
    Thread.sleep(Math.abs((MIN_SLEEP_TIME + random.nextInt()) % MAX_SLEEP_TIME));
  }

  boolean areThereActiveThreads() {
    boolean isThereActiveThreads = false;
    for (Thread t : threads) {
      isThereActiveThreads = isThereActiveThreads || t.isAlive();
    }
    return isThereActiveThreads;
  }

  List<Integer> toList(ConcurrentLinkedQueue<Integer> concurrentLinkedQueue) {
    return new ArrayList<>(concurrentLinkedQueue);
  }

  List<Integer> sort(List<Integer> list) {
    List<Integer> copy = new ArrayList<>(list);
    copy.sort(Comparator.comparingInt(a -> a));
    return copy;
  }
}
