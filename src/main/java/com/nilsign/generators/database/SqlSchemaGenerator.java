package com.nilsign.generators.database;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.misc.Pair;
import lombok.NonNull;

public class SqlSchemaGenerator {

  public static final String SQL_PRIMARY_KEY_NAME = "id";
  private static final String SQL_TABLE_PREFIX = "tbl";

  public static String buildTableName(@NonNull DxdEntityClass entityClass) {
    return String.format("%s_%s", SQL_TABLE_PREFIX, entityClass.getName());
  }

  public static String buildTableName(@NonNull DxdEntityRelation relation) {
    return String.format("%s_%s",
        buildTableName(relation.getReferencingClass()),
        buildTableName(relation.getReferencedClass()));
  }

  public static String buildForeignKeyName(@NonNull String referencedClassName) {
    return String.format("%s_%s", SQL_PRIMARY_KEY_NAME, referencedClassName);
  }

  public static Pair<String, String> buildForeignKeyNames(@NonNull DxdEntityRelation relation) {
    return Pair.of(
        String.format("%s_%s",
            SQL_PRIMARY_KEY_NAME,
            relation.getReferencedClass().getName()),
         String.format("%s_%s",
            SQL_PRIMARY_KEY_NAME,
            relation.getReferencingClass().getName()));
  }
}
