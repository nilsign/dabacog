package com.nilsign.dxd.model;

public enum DxdFieldRelationType {
  MANY_TO_MANY,
  MANY_TO_ONE,
  ONE_TO_MANY,
  ONE_TO_ONE;

  private static final String N_TO_N = "n..n";
  private static final String N_TO_I = "n..1";
  private static final String I_TO_N = "1..n";
  private static final String I_TO_I = "1..1";

  public static DxdFieldRelationType of(String type) {
    if (type == null) {
      return null;
    }
    type = type.trim().toLowerCase();
    if (type.equals(N_TO_N)) {
      return DxdFieldRelationType.MANY_TO_MANY;
    }
    if (type.equals(N_TO_I)) {
      return DxdFieldRelationType.MANY_TO_ONE;
    }
    if (type.equals(I_TO_N)) {
      return DxdFieldRelationType.ONE_TO_MANY;
    }
    if (type.equals(I_TO_I)) {
      return DxdFieldRelationType.ONE_TO_ONE;
    }
    return null;
  }

  public boolean isManyToMany() {
    return this == DxdFieldRelationType.MANY_TO_MANY;
  }

  public boolean isManyToOne() {
    return this == DxdFieldRelationType.MANY_TO_ONE;
  }

  public boolean isOneToMany() {
    return this == DxdFieldRelationType.ONE_TO_MANY;
  }

  public boolean isOneToOne() {
    return this == DxdFieldRelationType.ONE_TO_ONE;
  }

  public String getShortForm() {
    switch(this) {
      case MANY_TO_MANY: return N_TO_N;
      case MANY_TO_ONE: return N_TO_I;
      case ONE_TO_MANY: return I_TO_N;
      case ONE_TO_ONE: return I_TO_I;
      default: return null;
    }
  }
}
