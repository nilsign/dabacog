package com.nilsign.generators.sql;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xml.entities.DxdEntityClass;
import lombok.NonNull;

public class SqlSchemaGenerator {

  private static final String SQL_TABLE_PREFIX = "tbl";
  private static final String SQL_PRIMARY_KEY_NAME = "id";

  public static String buildTableName(@NonNull DxdEntityClass entityClass) {
    return String.format("%s_%s", SQL_TABLE_PREFIX, entityClass.getName());
  }

  public static String buildManyToManyTableName(@NonNull DxdEntityRelation relation) {
    return String.format("%s_%s",
        buildTableName(relation.getReferencingClass()),
        buildTableName(relation.getReferencedClass()));
  }

  public static String buildForeignKeyName(@NonNull String referencedTableName) {
    return String.format("%s_%s", SQL_PRIMARY_KEY_NAME, referencedTableName);
  }

}
