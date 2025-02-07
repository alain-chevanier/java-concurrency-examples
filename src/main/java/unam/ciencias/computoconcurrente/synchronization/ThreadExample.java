package unam.ciencias.computoconcurrente.synchronization;

public class ThreadExample {
  static int counter = 0;

  public static void main(String[] args) throws Exception {
    exampleRaceCondition();
    // exampleUserAndDaemonThreads();
  }

  static void exampleRaceCondition() throws InterruptedException {
    counter = 0;

    var t1 = new Thread(() -> incrementCounter());
    var t2 = new Thread(ThreadExample::incrementCounter);

    t2.start();
    t1.start();

    t1.join();
    t2.join();

    // this always gets an unpredictable result
    // the value should be 2,000,000
    System.out.println("counter value: " + counter);
  }

  static void incrementCounter() {
    for (int i = 0; i < 1000000; i++) {
      counter++;
    }
  }

  static void exampleUserAndDaemonThreads() throws Exception {
      Runnable runnable  = () -> {
        System.out.println("Thread 1: Hello World");
    };
    var thread = new Thread(runnable);


    var daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("Deamon thread: Hello");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
    });
    daemonThread.setDaemon(true);

    daemonThread.start();
    thread.start();

    Thread.sleep(1000);
    System.out.println("Main thread Finishing my execution");
  }
}
