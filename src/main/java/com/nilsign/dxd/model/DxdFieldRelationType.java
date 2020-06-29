package com.nilsign.dxd.model;

public enum DxdFieldRelationType {
  MANY_TO_MANY,
  MANY_TO_ONE,
  ONE_TO_MANY,
  ONE_TO_ONE;

  public static DxdFieldRelationType of(String type) {
    if (type == null) {
      return null;
    }
    type = type.trim().toLowerCase();
    if (type.equals("n..n")) {
      return DxdFieldRelationType.MANY_TO_MANY;
    }
    if (type.equals("n..1")) {
      return DxdFieldRelationType.MANY_TO_ONE;
    }
    if (type.equals("1..n")) {
      return DxdFieldRelationType.ONE_TO_MANY;
    }
    if (type.equals("1..1")) {
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
}
