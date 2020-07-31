package com.nilsign.misc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public final class Pair<T, S> {

  private T first;
  private S second;
}
