package unam.ciencias.computoconcurrente.spinlocks;

public interface Counter {

  int getAndIncrement();

  int getAndDecrement();

  int getValue();
}
