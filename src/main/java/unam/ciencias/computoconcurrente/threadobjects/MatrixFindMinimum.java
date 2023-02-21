package unam.ciencias.computoconcurrente.threadobjects;

public interface MatrixFindMinimum {
  <N extends Comparable<N>> N findMinimum(Matrix<N> matrix) throws InterruptedException;
}
