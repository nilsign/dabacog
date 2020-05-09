package com.nilsign.misc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class Pair<T, S> {

  private T first;
  private S second;

  public Pair<S, T> invert() {
    return of(second, first);
  }
}
