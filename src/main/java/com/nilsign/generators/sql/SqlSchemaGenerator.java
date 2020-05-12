package com.nilsign.generators.sql;

import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.misc.Pair;

public class SqlSchemaGenerator {

  private static final String SQL_TABLE_PREFIX = "tbl";
  private static final String SQL_PRIMARY_KEY_NAME = "id";

  public static String buildTableName(DxdEntityClass entityClass) {
    return String.format("%s_%s", SQL_TABLE_PREFIX, entityClass.getName());
  }

  public static String buildManyToManyTableName(Pair<DxdEntityClass, DxdEntityClass> relation) {
    return String.format("%s_%s",
        buildTableName(relation.getFirst()),
        buildTableName(relation.getSecond()));
  }

  public static String buildForeignKeyName(String referencedTableName) {
    return String.format("%s_%s", SQL_PRIMARY_KEY_NAME, referencedTableName);
  }

}
