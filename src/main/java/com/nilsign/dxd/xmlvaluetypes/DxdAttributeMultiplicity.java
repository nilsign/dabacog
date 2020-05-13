package com.nilsign.dxd.xmlvaluetypes;

import lombok.NonNull;

public enum DxdAttributeMultiplicity {
  ONE("one"),
  MANY("many");

  private final String value;

  DxdAttributeMultiplicity(@NonNull String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value.toUpperCase();
  }
}
