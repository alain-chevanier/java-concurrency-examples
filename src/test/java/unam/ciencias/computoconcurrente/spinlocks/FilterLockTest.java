package unam.ciencias.computoconcurrente.spinlocks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

@DisabledIfEnvironmentVariable(named = "ENV", matches = "github")
public class FilterLockTest extends BaseTestSuite {
  @Test
  void twoThreaded() throws InterruptedException {
    LockTestExecutor.performTest(new FilterLock(2), 2);
  }

  @Test
  @DisableIfHasFewerThanFourCores
  void fourThreaded() throws InterruptedException {
    LockTestExecutor.performTest(new FilterLock(4), 4);
  }

  @Test
  @DisableIfHasFewerThanEightCores
  void eightThreaded() throws InterruptedException {
    LockTestExecutor.performTest(new FilterLock(8), 8);
  }

  @Test
  @EnableIfHasNotDefaultCores
  void maximumNumberOfThreads() throws InterruptedException {
    LockTestExecutor.performTest(
        new FilterLock(Runtime.getRuntime().availableProcessors()),
        Runtime.getRuntime().availableProcessors());
  }
}
