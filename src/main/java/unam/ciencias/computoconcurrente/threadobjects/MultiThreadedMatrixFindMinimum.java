package unam.ciencias.computoconcurrente.threadobjects;

import java.util.ArrayList;
import java.util.Arrays;
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
  public <N extends Comparable<N>> N findMinimum(Matrix<N> matrix)
    throws InterruptedException {
    List<N> minimums = createMinimumsList();
    List<Thread> threadList = createThreadList(matrix, minimums);
    this.runThreads(threadList);
    this.waitThreads(threadList);
    return minimums.stream().min(N::compareTo)
      .orElseThrow(NoSuchElementException::new);
  }

  private <N extends Comparable<N>> List<N>
    createMinimumsList() {
    List<N> minimums = new ArrayList<>(this.threads);
    for (int i = 0; i < this.threads; i++) {
      minimums.add(null);
    }
    return minimums;
  }

  private <N extends Comparable<N>> List<Thread>
    createThreadList(Matrix<N> matrix, List<N> minimums) {
    List<Thread> threadList = new ArrayList<>(this.threads);
    for (int i = 0; i < this.threads; i++) {
      int threadId = i;
      threadList.add(new Thread(() ->
                                taskFindMinimum(threadId, matrix, minimums)));
    }
    return threadList;
  }

  private <N extends Comparable<N>> void taskFindMinimum(
      int threadId, Matrix<N> matrix, List<N> minimums) {
    int chunkSize = matrix.getRows() / this.threads;
    int initRow = threadId * chunkSize;
    // in case matrix.getRows() % chunkSize != 0,
    // the last thread process the remaning rows
    int remainingWork = threadId == this.threads - 1 ?
                          matrix.getRows() % chunkSize : 0;
    int lastRow = initRow + chunkSize + remainingWork;
    N min = null;
    for (int i = initRow; i < lastRow; i++) {
      N rowMin = this.findMinimumInRow(matrix.getRow(i));
      if (Objects.isNull(min) ||
          rowMin.compareTo(min) < 0) {
        min = rowMin;
      }
    }
    minimums.set(threadId, min);
  }

  private <N extends Comparable<N>> N findMinimumInRow(List<N> values) {
    N minimum = null;
    for (N value : values) {
      if (Objects.isNull(minimum) ||
          value.compareTo(minimum) < 0) {
        minimum = value;
      }
    }
    return minimum;
  }
}
