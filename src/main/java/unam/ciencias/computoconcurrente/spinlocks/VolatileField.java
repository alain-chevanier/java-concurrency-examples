package unam.ciencias.computoconcurrente.spinlocks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VolatileField<T> {
  private volatile T value;

  public VolatileField(T value) {
    this.value = value;
  }
}
