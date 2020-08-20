package com.nilsign.generators.database.postgresql;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.generators.database.Sql;
import com.nilsign.misc.Pair;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PostgreSqlTableBuilder {

  public static String buildRelationalTable(@NonNull DxdFieldRelation relation) {
    Pair<String, String> foreignKeyNames = Sql.buildForeignKeyFieldNames(relation);
    return new StringBuffer()
        .append(String.format(
            "\nCREATE TABLE IF NOT EXISTS %s_%s (",
            Sql.buildTableName(relation.getFirstClass()),
            Sql.buildTableName(relation.getSecondClass())))
        .append(String.format(
            "\n    %s BIGINT NOT NULL,",
            foreignKeyNames.getFirst()))
        .append(String.format(
            "\n    %s BIGINT NOT NULL,",
            foreignKeyNames.getSecond()))
        .append(String.format(
            "\n    CONSTRAINT %s PRIMARY KEY (%s, %s)",
            Sql.buildConstraintsNameForRelationalTablePrimaryKeyFields(relation),
            foreignKeyNames.getFirst(),
            foreignKeyNames.getSecond()))
        .append("\n);\n")
        .toString();
  }

  public static String buildTable(@NonNull DxdClass aClass) {
    StringBuffer output = new StringBuffer()
        .append(String.format(
            "\nCREATE TABLE IF NOT EXISTS %s (",
            Sql.buildTableName(aClass)))
        .append(buildPrimaryKeyField(aClass));
    aClass.getFields().forEach(field
        -> output.append(buildField(aClass, field)));
    return output
        .append("\n);\n")
        .toString();
  }

  private static String buildPrimaryKeyField(@NonNull DxdClass aClass) {
    return String.format(
        "\n    %s BIGINT CONSTRAINT %s PRIMARY KEY",
        Sql.SQL_PRIMARY_KEY_NAME,
        Sql.buildConstraintsNameForPrimaryKeyField(aClass));
  }

  private static String buildField(@NonNull DxdClass aClass, @NonNull DxdField field) {
    return String.format(
        ",\n    %s %s%s%s%s",
        Sql.buildFieldName(field),
        buildDataType(field),
        buildNotNull(field),
        buildUnique(aClass, field),
        buildDefaultValue(field));
  }

  private static String buildDataType(@NonNull DxdField field) {
    String dataType =
        field.getType().isObject() ? "BIGINT" :
        field.getType().isString() ? "TEXT" :
        field.getType().isLong() ? "BIGINT" :
        field.getType().isInt() ? "INTEGER" :
        field.getType().isDouble() ? "NUMERIC" :
        field.getType().isFloat() ? "NUMERIC" :
        field.getType().isBoolean() ? "BOOLEAN" :
        field.getType().isDate() ? "TIMESTAMP" :
        field.getType().isBlob() ? "BYTEA" :
        null;
    if (dataType == null) {
      throw new RuntimeException(String.format(
          "Unsupported Sql datatype '%s'.",
          field.getType().getName()));
    }
    return dataType;
  }

  private static String buildNotNull(@NonNull DxdField field) {
    return !field.isNullable()
        ? " NOT NULL"
        : "";
  }

  private static String buildUnique(@NonNull DxdClass aClass, @NonNull DxdField field) {
    return field.isUnique()
        ? String.format(
            " CONSTRAINT %s UNIQUE",
            Sql.buildConstraintsNameForUniqueField(aClass, field))
        : "";
  }

  private static String buildDefaultValue(@NonNull DxdField field) {
    return field.hasDefaultValue()
        ? String.format(" DEFAULT %s", field.getDefaultValue())
        : "";
  }
}
