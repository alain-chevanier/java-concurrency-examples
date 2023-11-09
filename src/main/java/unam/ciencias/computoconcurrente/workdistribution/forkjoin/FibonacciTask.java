package unam.ciencias.computoconcurrente.workdistribution.forkjoin;

import java.util.concurrent.RecursiveTask;

public class FibonacciTask extends RecursiveTask<Integer> {

  int arg;

  public FibonacciTask(int n) {
    arg = n;
  }

  public Integer compute() {
    try {
      if (arg > 1) {
        FibonacciTask rightTask = new FibonacciTask(arg - 1);
        rightTask.fork(); // metete al thread pool donde ejecutamos la primer tarea.
        FibonacciTask leftTask = new FibonacciTask(arg - 2);
        leftTask.fork();
        return rightTask.join() + leftTask.join(); // + leftTask.compute();
      } else {
        return 1;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return 1;
    }
  }
}
