package unam.ciencias.computoconcurrente.threadobjects;

public interface MatrixConsumer<T> {
  void accept(int row, int column, T value);
}
