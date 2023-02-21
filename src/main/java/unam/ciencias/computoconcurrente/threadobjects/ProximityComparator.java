package unam.ciencias.computoconcurrente.threadobjects;

public interface ProximityComparator<T> {
  boolean test(T a, T b, float epsilon);
}
