package com.nilsign.dxd.xmlvaluetypes;

import lombok.NonNull;

public enum DxdFieldMultiplicity {
  ONE("one"),
  MANY("many");

  private final String value;

  DxdFieldMultiplicity(@NonNull String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value.toUpperCase();
  }
}
