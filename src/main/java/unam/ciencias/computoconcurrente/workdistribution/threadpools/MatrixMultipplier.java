package unam.ciencias.computoconcurrente.workdistribution.threadpools;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatrixMultipplier {

  static ExecutorService exec = Executors.newCachedThreadPool();

  static Matrix multiply(Matrix a, Matrix b) throws InterruptedException, ExecutionException {
    int n = a.getDim();
    Matrix c = new Matrix(n);
    Future<?> future = exec.submit(new MulTask(a, b, c));
    future.get();
    return c;
  }

  static class MulTask implements Runnable {
    Matrix a, b, c, lhs, rhs;

    public MulTask(Matrix a, Matrix b, Matrix c) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.lhs = new Matrix(a.getDim());
      this.rhs = new Matrix(a.getDim());
    }

    public void run() {
      try {
        if (a.getDim() == 1) {
          c.set(0, 0, a.get(0, 0) * b.get(0, 0));
        } else {
          Matrix[][] aa = a.split(), bb = b.split(), cc = c.split();
          Matrix[][] ll = lhs.split(), rr = rhs.split();
          Future<?>[][][] future = (Future<?>[][][]) new Future[2][2][2];
          // launch parallel multiplications
          for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++) {
              future[i][j][0] = exec.submit(new MulTask(aa[i][0], bb[0][i], ll[i][j]));
              future[i][j][1] = exec.submit(new MulTask(aa[1][i], bb[i][1], rr[i][j]));
            }
          // wait for them to finish
          for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++) for (int k = 0; k < 2; k++) future[i][j][k].get();
          // do sum
          Future<?> done = exec.submit(new MatrixAdder.AddTask(lhs, rhs, c));
          done.get();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
