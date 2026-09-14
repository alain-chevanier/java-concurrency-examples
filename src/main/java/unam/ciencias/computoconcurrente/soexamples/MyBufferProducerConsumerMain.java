package unam.ciencias.computoconcurrente.soexamples;

/** Runs multiple producers and consumers against a bounded {@link MyBuffer}. */
public final class MyBufferProducerConsumerMain {
  private static final int PRODUCER_COUNT = 2;
  private static final int CONSUMER_COUNT = 2;
  private static final int ITEMS_PER_PRODUCER = 10;
  private static final int STOP = Integer.MIN_VALUE;

  private MyBufferProducerConsumerMain() {}

  public static void main(String[] args) throws InterruptedException {
    Buffer<Integer> buffer = new MyBuffer<>(3);
    Thread[] consumers = new Thread[CONSUMER_COUNT];
    Thread[] producers = new Thread[PRODUCER_COUNT];

    for (int index = 0; index < CONSUMER_COUNT; index++) {
      consumers[index] = new Thread(() -> consume(buffer), "consumer-" + index);
      consumers[index].start();
    }

    for (int index = 0; index < PRODUCER_COUNT; index++) {
      int producerId = index;
      producers[index] = new Thread(
          () -> produce(buffer, producerId), "producer-" + producerId);
      producers[index].start();
    }

    for (Thread producer : producers) {
      producer.join();
    }
    for (int index = 0; index < CONSUMER_COUNT; index++) {
      buffer.put(STOP);
    }
    for (Thread consumer : consumers) {
      consumer.join();
    }

    System.out.println("All producers and consumers finished.");
  }

  private static void produce(Buffer<Integer> buffer, int producerId) {
    try {
      for (int value = 0; value < ITEMS_PER_PRODUCER; value++) {
        int item = producerId * ITEMS_PER_PRODUCER + value;
        buffer.put(item);
        System.out.printf("%s produced %d%n", Thread.currentThread().getName(), item);
      }
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
    }
  }

  private static void consume(Buffer<Integer> buffer) {
    try {
      while (true) {
        int item = buffer.take();
        if (item == STOP) {
          return;
        }
        System.out.printf("%s consumed %d%n", Thread.currentThread().getName(), item);
      }
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
    }
  }
}