package com.nilsign.dxd.model;

import lombok.Getter;
import lombok.NonNull;
import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public final class DxdFieldType {

  public static final String LONG_TYPE_NAME = "long";

  private enum FieldType {
    LONG(DxdFieldType.LONG_TYPE_NAME),
    DOUBLE("double"),
    INT("int"),
    FLOAT("float"),
    STRING("string"),
    BOOLEAN("boolean"),
    DATE("date"),
    BLOB("blob"),
    OBJECT("object");

    @NonNull
    private final String name;

    FieldType(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name.toUpperCase();
    }
  }

  @NonNull
  private final FieldType type;

  private final String objectName;

  public static DxdFieldType of(@NonNull String name) {
    return new DxdFieldType(name);
  }

  private DxdFieldType(@NonNull String name) {
    if (Arrays
        .stream(FieldType.values())
        .map(FieldType::toString)
        .collect(Collectors.toSet())
        .contains(name.toUpperCase())) {
      objectName = null;
      type = FieldType.valueOf(name.toUpperCase());
    } else {
      objectName = name;
      this.type = FieldType.OBJECT;
    }
  }

  public String getName() {
    return isObject()
        ? objectName
        : type.toString();
  }

  public boolean isLong() {
    return type == FieldType.LONG;
  }

  public boolean isInt() {
    return type == FieldType.INT;
  }

  public boolean isDouble() {
    return type == FieldType.DOUBLE;
  }

  public boolean isFloat() {
    return type == FieldType.FLOAT;
  }

  public boolean isBoolean() {
    return type == FieldType.BOOLEAN;
  }

  public boolean isString() {
    return type == FieldType.STRING;
  }

  public boolean isDate() {
    return type == FieldType.DATE;
  }

  public boolean isBlob() {
    return type == FieldType.BLOB;
  }

  public boolean isObject() {
    return type == FieldType.OBJECT;
  }
}
