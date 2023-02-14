package unam.ciencias.computoconcurrente;

public class App {

  public static void main(String[] a) throws InterruptedException {
    Thread runnable = new Thread(new HelloRunnable());
    runnable.start();

    Thread inheritThread = new HelloThread();
    inheritThread.start();

    Thread anonymousClassThread = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("Hello from anonymous class");
      }
    });
    anonymousClassThread.start();

    Thread lambdaThread = new Thread(App::sleepingRoutine);
    lambdaThread.start();

    Thread.sleep(5000);
    System.out.println("MAIN THREAD: Main thread will finish");

    SimpleThreads.example(a);
  }

  static void sleepingRoutine() {
    String importantInfo[] = {
            "Mares eat oats",
            "Does eat oats",
            "Little lambs eat ivy",
            "A kid will eat ivy too"
    };

    for (int i = 0;
         i < importantInfo.length;
         i++) {
      //Pause for 4 seconds
      try {
        Thread.sleep(4000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      //Print a message
      System.out.println("CHILD THREAD: " + importantInfo[i]);
    }
  }

}

class HelloRunnable implements Runnable {
  public void run() {
    System.out.println("Hello from a thread within runnable interface!");
  }
}

class HelloThread extends Thread {
  @Override
  public void run() {
    System.out.println("Hello from a thread from class that that extends Thread!");
  }
}