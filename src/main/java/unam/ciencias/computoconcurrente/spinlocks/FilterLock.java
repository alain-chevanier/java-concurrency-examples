package unam.ciencias.computoconcurrente.spinlocks;

/**
 * FilterLock
 */
public class FilterLock extends Lock {

  private final int threads;
  private final int[] threadLevel;
  private final VolatileInteger[] lastToArrive;

  public FilterLock(int threads) {
    this.threads = threads;
    threadLevel = new int[threads];
    lastToArrive = new VolatileInteger[threads];
    for(int i = 0; i < threadLevel.length; i++) {
      threadLevel[i] = -1;
      lastToArrive[i] = new VolatileInteger(-1);
    }
  }

  @Override
  public void lock() {
    int myId = this.threadID.get();
    for (int myLevel = 0; myLevel < this.threads; myLevel++) {
      this.threadLevel[myId] = myLevel;
      this.lastToArrive[myLevel].setValue(myId);
      while (lastToArrive[myLevel].getValue() == myId && existOtherThreadInHigherLevel(myId, myLevel)) {
        // keep spining
      }
    }
  }

  private boolean existOtherThreadInHigherLevel(int myThreadId, int myLevel) {
    for (int threadId = 0; threadId < this.threadLevel.length; threadId++) {
      if (threadId != myThreadId && this.threadLevel[threadId] >= myLevel) {
        return true;
      }
    }
    return false;
  }


  @Override
  public void unlock() {
    int myId = this.threadID.get();
    this.threadLevel[myId] = -1;
  }
}
