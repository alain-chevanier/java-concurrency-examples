package unam.ciencias.computoconcurrente.spinlocks;

public class VolatileBoolean {
  public volatile boolean value;

  public VolatileBoolean() {}

  public VolatileBoolean(boolean value) {
    this.value = value;
  }

  public static VolatileBoolean of(boolean value) {
    return new VolatileBoolean(value);
  }

  public boolean isValue() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }
}
