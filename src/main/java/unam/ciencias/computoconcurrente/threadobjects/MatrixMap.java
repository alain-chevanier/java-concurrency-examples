package unam.ciencias.computoconcurrente.threadobjects;

import java.util.function.Function;

public interface MatrixMap {

  <I, E> Matrix<E> map(Matrix<I> matrix, Function<I, E> function) throws InterruptedException;
}
