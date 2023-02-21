package unam.ciencias.computoconcurrente.threadobjects;

public interface MatrixReducer<T> {
  T reduce(int row, int column, T accValue, T value);
}
