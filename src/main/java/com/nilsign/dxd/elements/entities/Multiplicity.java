package com.nilsign.dxd.elements.entities;

public enum Multiplicity {
  ONE("one"),
  MANY("many");

  private final String value;

  Multiplicity(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value.toUpperCase();
  }
}
