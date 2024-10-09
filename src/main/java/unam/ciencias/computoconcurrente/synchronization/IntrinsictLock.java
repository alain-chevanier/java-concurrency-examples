package unam.ciencias.computoconcurrente.synchronization;

public class IntrinsictLock {
  int c1 = 0;
  int c2 = 0;

  Object lockC1 = new Object();
  Object lockC2 = new Object();

  int incrementC1() {
    synchronized(lockC1) {
      return c1++;
    }
  }

  int incrementC2() {
    synchronized(lockC2) {
      return c2++;
    }
  }
}
