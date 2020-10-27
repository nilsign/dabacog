package com.nilsign.generators.code.java;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.generators.Generator;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Java {

  public static String buildJavaFileName(String name) {
    return String.format("%s.java", normalizeClassName(name));
  }

  public static String buildGeneratedByComment() {
    return String.format("// %s", Generator.buildGeneratedByComment());
  }

  public String addPackage(@NonNull String name) {
    return String.format("\npackage %s;\n", name);
  }

  public String addImport(@NonNull String path) {
    return String.format("\nimport %s;", path);
  }

  public static String openClass(@NonNull DxdClass aClass) {
    return openClass(normalizeClassName(aClass.getName()));
  }

  public static String openClass(@NonNull String name) {
    return String.format("\npublic class %s {", name);
  }

  public static String declareField(@NonNull DxdField field) {
    return String.format(
        "\n  private %s %s;\n",
        buildJavaType(field),
        normalizeFieldName(field.getName()));
  }

  public static String addGetter(@NonNull DxdField field) {
    return new StringBuffer()
        .append(String.format(
            "\n  public %s get%s() {",
            buildJavaType(field),
            startUpperCased(field.getName())))
        .append(String.format(
            "\n    return %s;",
            normalizeFieldName(field.getName())))
        .append("\n}\n")
        .toString();
  }

  public static String closeClass() {
    return "\n}\n";
  }

  public static String buildJavaType(@NonNull DxdField field) {
      String dataType =
          field.getType().isObject() ? field.getType().getObjectName() :
          field.getType().isString() ? "String" :
          field.getType().isLong() ? "long" :
          field.getType().isInt() ? "int" :
          field.getType().isDouble() ? "double" :
          field.getType().isFloat() ? "float" :
          field.getType().isBoolean() ? "boolean" :
          field.getType().isDate() ? "Date" :
          field.getType().isBlob() ? "byte[]" :
          null;
      if (dataType == null) {
        throw new RuntimeException(String.format(
            "Unsupported Java datatype '%s'.",
            field.getType().getName()));
      }
      return dataType;
  }

  public static String normalizeClassName(@NonNull String name) {
    return startUpperCased(name);
  }

  private static String normalizeFieldName(@NonNull String name) {
    return String.format("%s%s", name.substring(0, 1).toLowerCase(), name.substring(1));
  }

  private static String startUpperCased(@NonNull String name) {
    return String.format("%s%s", name.substring(0, 1).toUpperCase(), name.substring(1));
  }
}
