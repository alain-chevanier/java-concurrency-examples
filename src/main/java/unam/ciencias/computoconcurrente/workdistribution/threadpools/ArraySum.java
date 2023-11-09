/*
 * ArraySum.java
 *
 * Created on December 9, 2006, 5:28 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package unam.ciencias.computoconcurrente.workdistribution.threadpools;

import java.util.concurrent.*;

/**
 * Divide-and-conquer parallel array summation (Exercise solution)
 *
 * @author Maurice Herlihy
 */
public class ArraySum {

  static ExecutorService exec = Executors.newCachedThreadPool();

  public static void main(String[] args) {

  }

  static int sum(int[] a) throws InterruptedException, ExecutionException {
    Future<Integer> future = exec.submit(() -> sumAux(a, 0, a.length));
    return future.get();
  }

  static Integer sumAux(int[] a, int start, int size)
    throws ExecutionException, InterruptedException {
    if (size == 500) {
      // usually we establish a larger threshold
      return a[start];
    } else {
      int lhsSize = size / 2;
      int rhsStart = lhsSize + 1;
      int rhsSize = size - lhsSize;
      Future<Integer> lhs = exec.submit(() -> sumAux(a, start, lhsSize));
      Future<Integer> rhs = exec.submit(() -> sumAux(a, rhsStart, rhsSize));
      return rhs.get() + lhs.get();
    }
  }

  static class AddAux implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
      return null;
    }
  }
}
