package unam.ciencias.computoconcurrente;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import unam.ciencias.computoconcurrente.threadobjects.Matrix;
import unam.ciencias.computoconcurrente.threadobjects.MatrixFindMinimum;
import unam.ciencias.computoconcurrente.threadobjects.MultiThreadedMatrixFindMinimum;

public class MatrixFindMinimumTest {
  MatrixFindMinimum matrixFindMinimum;

  @Test
  void findMinimum() throws Exception {
    matrixFindMinimum = new MultiThreadedMatrixFindMinimum();
    Matrix<Float> matrix =
        new Matrix<>(
            new Float[][] {
              {4f, 29f, -6f, 0f},
              {15f, 6f, 0f, 4f},
              {25f, 41f, -10f, 4f},
              {0f, 0f, -1f, 39f},
            });

    assertEquals(-10f, matrixFindMinimum.findMinimum(matrix));
  }

  @Test
  void findMinimumConcurrent() throws Exception {
    matrixFindMinimum = new MultiThreadedMatrixFindMinimum(2);
    Matrix<Float> matrix =
        new Matrix<>(
            new Float[][] {
              {4f, 29f, -6f, 0f},
              {15f, 6f, 0f, 4f},
              {25f, 41f, -10f, 4f},
              {0f, 0f, -1f, 39f},
            });

    assertEquals(-10f, matrixFindMinimum.findMinimum(matrix));
  }

  @Test
  void findMinimumBigMatrixSequential() throws Exception {
    executeBigMatrixTest(1, 1500, 1000);
  }

  @Test
  void findMinimumBigMatrixMultiThreaded() throws Exception {
    executeBigMatrixTest(Runtime.getRuntime().availableProcessors(), 1500, 1000);
  }

  private void executeBigMatrixTest(int threads, int rows, int columns)
      throws InterruptedException {
    MatrixFindMinimum matrixFindMinimum = new MultiThreadedMatrixFindMinimum(threads);
    Matrix<Float> matrix = new Matrix<>(rows, columns);
    float minimum = MatrixUtilsTestHelper.fillMatrixAndReturnMinimumValue(matrix);
    assertEquals(minimum, matrixFindMinimum.findMinimum(matrix));
  }
}
