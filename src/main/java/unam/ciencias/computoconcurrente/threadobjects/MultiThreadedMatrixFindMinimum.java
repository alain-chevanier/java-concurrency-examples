package unam.ciencias.computoconcurrente.threadobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MultiThreadedMatrixFindMinimum extends MultiThreadedOperation
    implements MatrixFindMinimum {

  public MultiThreadedMatrixFindMinimum() {
    super();
  }

  public MultiThreadedMatrixFindMinimum(int threads) {
    super(threads);
  }

  @Override
  public <N extends Comparable<N>> N findMinimum(Matrix<N> matrix) throws InterruptedException {
    List<N> minimumsList = new ArrayList<>(this.threads);
    List<Thread> threadList = new ArrayList<>(this.threads);
    for (int i = 0; i < this.threads; i++) {
      minimumsList.add(null);
    }
    for (int i = 1; i < this.threads; i++) {
      int threadId = i;
      threadList.add(new Thread(() -> taskFindMinimum(threadId, minimumsList, matrix)));
    }
    this.runThreads(threadList);
    taskFindMinimum(0, minimumsList, matrix);
    this.waitThreads(threadList);
    return minimumsList.stream().min(N::compareTo).orElseThrow(NoSuchElementException::new);
  }

  private <N extends Comparable<N>> void taskFindMinimum(
      int threadId, List<N> minimumsList, Matrix<N> matrix) {
    N min = null;
    int chunkSize = matrix.getRows() / this.threads;
    int initRow =
        threadId * chunkSize + (threadId == this.threads - 1 ? matrix.getRows() % chunkSize : 0);
    for (int i = initRow; i < initRow + chunkSize; i++) {
      N rowMin = this.findMinimumInRow(matrix.getRow(i));
      if (min == null) {
        min = rowMin;
      } else {
        min = rowMin.compareTo(min) < 0 ? rowMin : min;
      }
    }
    minimumsList.set(threadId, min);
  }

  private <N extends Comparable<N>> N findMinimumInRow(List<N> values) {
    N minimum = null;
    for (N value : values) {
      if (Objects.isNull(minimum)) {
        minimum = value;
        continue;
      }
      if (value.compareTo(minimum) < 0) {
        minimum = value;
      }
    }
    return minimum;
  }
}
