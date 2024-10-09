package unam.ciencias.computoconcurrente.blockingsynchronization;

public class MainBoundedBuffer {
  static final int CAPACITY = 20;
  static BoundedBuffer<Integer> boundedBuffer
    = new BoundedBufferImpl<>(CAPACITY);

  public static void main(String[] args)
    throws InterruptedException {
    boundedBuffer.put(5);
  }
}
