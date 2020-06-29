package com.nilsign.dxd.model;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
public final class DxdFieldRelation {

  @NonNull
  private final DxdClass firstClass;

  @NonNull
  private final DxdField firstField;

  @NonNull
  private final DxdClass secondClass;

  private final DxdField secondField;

  @NonNull
  private final DxdFieldRelationType type;

  public boolean isBiDirectional() {
    return secondField != null;
  }

  public boolean isOneDirectional() {
    return secondField == null;
  }

  public boolean isManyToMany() {
    return type.isManyToMany();
  }

  public boolean isManyToOne() {
    return type.isManyToOne();
  }

  public boolean isOneToMany() {
    return type.isOneToMany();
  }

  public boolean isOneToOne() {
    return type.isOneToOne();
  }

  public boolean isSelfReference() {
    return firstClass == secondClass;
  }
}
