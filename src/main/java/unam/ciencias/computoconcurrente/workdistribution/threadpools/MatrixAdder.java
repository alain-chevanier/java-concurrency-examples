/*
 * MatrixTask.java
 *
 * Created on January 23, 2006, 8:53 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package unam.ciencias.computoconcurrente.workdistribution.threadpools;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Parallel Matrix Multiplication
 *
 * @author Maurice Herlihy
 */
public class MatrixAdder {
  static ExecutorService exec = Executors.newCachedThreadPool();

  public static void main() {}

  static Matrix add(Matrix a, Matrix b) throws InterruptedException, ExecutionException {
    int n = a.getDim();
    Matrix c = new Matrix(n);
    Future<?> future = exec.submit(new AddTask(a, b, c));
    future.get();
    return c;
  }

  static class AddTask implements Runnable {
    Matrix a, b, c;

    public AddTask(Matrix a, Matrix b, Matrix c) {
      this.a = a;
      this.b = b;
      this.c = c;
    }

    public void run() {
      try {
        int n = a.getDim();
        if (n == 1) {
          // n <= 100
          c.set(0, 0, a.get(0, 0) + b.get(0, 0));
        } else {
          Matrix[][] aa = a.split(), bb = b.split(), cc = c.split();
          Future<?>[][] future = (Future<?>[][]) new Future[2][2];
          // create asynchronous computations
          for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
              future[i][j] = exec.submit(new AddTask(aa[i][j], bb[i][j], cc[i][j]));
          // wait for them to finish
          for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
              future[i][j].get();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
