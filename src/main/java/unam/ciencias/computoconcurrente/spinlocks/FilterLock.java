package unam.ciencias.computoconcurrente.spinlocks;

/**
 * FilterLock
 */
public class FilterLock extends Lock {

  private final int threads;

  public FilterLock(int threads) {
    this.threads = threads;
  }


  @Override
  public void lock() {

  }

  @Override
  public void unlock() {

  }
}
