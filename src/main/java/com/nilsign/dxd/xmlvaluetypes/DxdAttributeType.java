package com.nilsign.dxd.xmlvaluetypes;

import lombok.NonNull;

public enum DxdAttributeType {
  LONG("long"),
  DOUBLE("double"),
  INT("int"),
  FLOAT("float"),
  STRING("string"),
  BOOLEAN("boolean"),
  DATE("date"),
  BLOB("blob");

  private final String value;

  DxdAttributeType(@NonNull String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value.toUpperCase();
  }
}
