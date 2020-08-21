package com.nilsign.generators.database.postgresql;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.generators.database.Sql;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PostgreSqlIndexBuilder {

  public static String buildIndex(@NonNull DxdClass aClass, @NonNull DxdField field) {
    return field.hasRelation() && !field.getRelationType().isManyToMany()
        ? buildIndex(
            Sql.buildIndexNameForForeignKeyField(aClass, field),
            Sql.buildTableName(aClass),
            Sql.buildForeignKeyFieldName(field.getName()))
        : field.isIndexed()
        ? buildIndex(
            Sql.buildIndexNameForField(aClass, field),
            Sql.buildTableName(aClass),
            Sql.buildFieldName(field))
        : "";
  }

  private static String buildIndex(
      @NonNull String indexName,
      @NonNull String tableName,
      @NonNull String fieldName) {
    return String.format(
        "\nCREATE INDEX IF NOT EXISTS %s ON %s(%s);",
        indexName,
        tableName,
        fieldName);
  }
}
