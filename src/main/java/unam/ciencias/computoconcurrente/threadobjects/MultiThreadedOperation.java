package unam.ciencias.computoconcurrente.threadobjects;

import java.util.List;

public class MultiThreadedOperation {
  protected final int threads;

  public MultiThreadedOperation() {
    this.threads = 1;
  }

  public MultiThreadedOperation(int threads) {
    this.threads = threads;
  }

  protected void runThreads(List<Thread> threads) throws InterruptedException {
    threads.forEach(Thread::start);
  }

  protected void waitThreads(List<Thread> threads) throws InterruptedException {
    for (Thread t : threads) {
      t.join();
    }
  }
}
