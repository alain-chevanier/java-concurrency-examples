package unam.ciencias.computoconcurrente.threadobjects;

public class SimpleThreads {

  // Display a message, preceded by
  // the name of the current thread
  public static void main(String args[]) throws InterruptedException {

    // Delay, in milliseconds before
    // we interrupt MessageLoop
    // thread (default one hour).
    long patience = 1000 * 10; // 60 * 60;


    threadMessage("Starting MessageLoop thread");
    long startTime = System.currentTimeMillis();

    Thread childThread = new Thread(new MessageLoop());
    childThread.start();

    threadMessage("Waiting for MessageLoop thread to finish");
    // loop until MessageLoop
    // thread exits
    while (childThread.isAlive()) {
      threadMessage("Still waiting...");
      // Wait maximum of 1 second
      // for MessageLoop thread
      // to finish.
      childThread.join(1000);

      if (amIDoneWaiting(patience, startTime, childThread)) {
        threadMessage("Tired of waiting!");
        childThread.interrupt();
        // Shouldn't be long now
        // -- wait indefinitely
        childThread.join();
      }
    }
    threadMessage("Finally!");
  }

  private static boolean amIDoneWaiting(long patience, long startTime, Thread t) {
    long elapsedTime = System.currentTimeMillis() - startTime;
    return (elapsedTime > patience) && t.isAlive();
  }

  static void threadMessage(String message) {
    String threadName = Thread.currentThread().getName();
    System.out.format("%s: %s%n", threadName, message);
  }

  private static class MessageLoop implements Runnable {
    @Override
    public void run() {
      String[] importantInfo = {
        "Mares eat oats",
        "Does eat oats",
        "Little lambs eat ivy",
        "A kid will eat ivy too"
      };
      // wait using sleep
      try {
        for (String str : importantInfo) {
          // Pause for ~4 seconds
          Thread.sleep(4000);
          // Print a message
          threadMessage(str);
        }
      } catch (InterruptedException e) {
        threadMessage("I wasn't done!");
      }
     }
  }
}
