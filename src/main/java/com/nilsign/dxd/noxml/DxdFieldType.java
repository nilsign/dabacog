package com.nilsign.dxd.noxml;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class DxdFieldType {

  public static String LONG_TYPE_NAME = "long";

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

    private final String name;

    FieldType(@NonNull String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name.toUpperCase();
    }
  }

  private final String typeName;
  private final FieldType type;

  public DxdFieldType(String name) {
    Set<String> test = Arrays
        .stream(FieldType.values())
        .map(FieldType::toString)
        .collect(Collectors.toSet());
    if (Arrays
        .stream(FieldType.values())
        .map(FieldType::toString)
        .collect(Collectors.toSet())
        .contains(name.toUpperCase())) {
      typeName = null;
      type = FieldType.valueOf(name.toUpperCase());
    } else {
      typeName = name;
      this.type = FieldType.OBJECT;
    }
  }

  public String getTypeName() {
    return isObject()
        ? typeName
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
