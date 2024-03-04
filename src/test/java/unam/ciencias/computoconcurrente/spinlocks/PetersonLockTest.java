package unam.ciencias.computoconcurrente.spinlocks;

import org.junit.jupiter.api.Test;

public class PetersonLockTest {

  @Test
  void twoThreaded() throws InterruptedException {
    LockTestExecutor.performTest(new PetersonLock(), 2);
  }
}
