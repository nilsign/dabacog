package com.nilsign.dxd.xmlvaluetypes;

import lombok.NonNull;

public enum Multiplicity {
  ONE("one"),
  MANY("many");

  private final String value;

  Multiplicity(@NonNull String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value.toUpperCase();
  }
}
