package unam.ciencias.computoconcurrente.spinlocks;

/**
 * FilterLock
 */
public class FilterLock implements Lock {

  private final int threads;
  private final VolatileInteger[] threadLevel;
  private final VolatileInteger[] lastThreadToArrive;

  public FilterLock(int threads) {
    this.threads = threads;
    this.threadLevel = new VolatileInteger[threads];
    this.lastThreadToArrive = new VolatileInteger[threads];
    for (int i = 0; i < threadLevel.length; i++) {
      this.threadLevel[i] = new VolatileInteger(-1);
      this.lastThreadToArrive[i] = new VolatileInteger(-1);
    }
  }

  @Override
  public void lock() {
    int myId = ThreadID.get();
    for (int myLevel = 0; myLevel < this.threads; myLevel++) {
      this.threadLevel[myId].setValue(myLevel);
      this.lastThreadToArrive[myLevel].setValue(myId);
      while (lastThreadToArrive[myLevel].getValue() == myId
          && existOtherThreadInHigherLevel(myId, myLevel)) {
        // keep spining
      }
    }
  }

  private boolean existOtherThreadInHigherLevel(int myThreadId, int myLevel) {
    for (int threadId = 0; threadId < this.threadLevel.length; threadId++) {
      if (threadId != myThreadId && this.threadLevel[threadId].getValue() >= myLevel) {
        return true;
      }
    }
    return false;
  }


  @Override
  public void unlock() {
    int myId = ThreadID.get();
    this.threadLevel[myId].setValue(-1);
  }
}
