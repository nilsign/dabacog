package com.nilsign.generators.database;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.misc.Pair;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Sql {

  public static final String SQL_PRIMARY_KEY_NAME = "id";

  private static final String SQL_TABLE_PREFIX = "tbl";
  private static final String SQL_CONSTRAINT_PREFIX = "cstr";
  private static final String SQL_INDEX_PREFIX = "idx";

  public static String buildTableName(@NonNull DxdClass aClass) {
    return buildTableName(aClass.getName());
  }

  public static String buildTableName(@NonNull String className) {
    return String.format("%s_%s",
        SQL_TABLE_PREFIX,
        transformName(className));
  }

  public static String buildTableName(@NonNull DxdFieldRelation relation) {
    return String.format("%s_%s",
        buildTableName(relation.getFirstClass()),
        buildTableName(relation.getSecondClass()));
  }

  public static String buildFieldName(@NonNull DxdField field) {
    return field.getType().isObject()
        ? buildForeignKeyName(field.getName())
        : transformName(field.getName());
  }

  public static String buildForeignKeyName(@NonNull String referencedClassName) {
    return String.format("%s_%s",
        SQL_PRIMARY_KEY_NAME,
        transformName(referencedClassName));
  }

  public static Pair<String, String> buildForeignKeyNames(@NonNull DxdFieldRelation relation) {
    return Pair.of(
        String.format("%s_%s",
            SQL_PRIMARY_KEY_NAME,
            transformName(relation.getFirstClass().getName())),
         String.format("%s_%s",
            SQL_PRIMARY_KEY_NAME,
             transformName(relation.getSecondClass().getName())));
  }

  public static String buildConstraintsNameForPrimaryKeyField(@NonNull DxdClass aClass) {
    return String.format(
        "%s_%s_pk",
        SQL_CONSTRAINT_PREFIX,
        buildTableName(aClass.getName()));
  }

  public static String buildConstraintsNameForForeignKeyField(
      @NonNull DxdClass aClass,
      @NonNull DxdField field) {
    return String.format(
        "%s_%s_%s_fk",
        SQL_CONSTRAINT_PREFIX,
        buildTableName(aClass.getName()),
        transformName(field.getName()));
  }

  public static String buildConstraintsNameForUniqueField(
      @NonNull DxdClass aClass,
      @NonNull DxdField field) {
    return String.format(
        "%s_%s_%s_unique",
        SQL_CONSTRAINT_PREFIX,
        buildTableName(aClass.getName()),
        transformName(field.getName()));
  }

  public static String buildConstraintsNameForRelationalTablePrimaryKeyFields(
      @NonNull DxdFieldRelation relation) {
    return String.format(
        "%s_%s_%s_pks",
        SQL_CONSTRAINT_PREFIX,
        buildTableName(relation.getFirstClass().getName()),
        buildTableName(relation.getSecondClass().getName()));
  }

  public static String buildConstraintsNameForRelationalTableForeignKeyField(
      @NonNull DxdFieldRelation relation,
      @NonNull DxdClass aClass) {
    return String.format(
        "%s_%s_%s_fk",
        SQL_CONSTRAINT_PREFIX,
        buildTableName(relation),
        buildForeignKeyName(aClass.getName())
    );
  }

  public static String buildIndexNameForForeignKeyField(
      @NonNull DxdClass aClass,
      @NonNull DxdField field) {
    return String.format(
        "idx_%s_%s",
        buildTableName(aClass.getName()),
        buildForeignKeyName(field.getName())
    );
  }

  public static String buildIndexNameForForeignKeyField(
      @NonNull DxdFieldRelation relation,
      @NonNull DxdClass referencedClass) {git d
    return String.format(
        "%s_%s_%s",
        SQL_INDEX_PREFIX,
        buildTableName(relation),
        buildForeignKeyName(referencedClass.getName()));
  }

  private static String transformName(@NonNull String name) {
    String normalizedName = "";
    for (int i = 0; i < name.length(); ++i) {
      if (i > 0
          && Character.isUpperCase(name.charAt(i))
          && Character.isLowerCase(name.charAt(i - 1))
          || i > 1
          && Character.isLowerCase(name.charAt(i))
          && Character.isUpperCase(name.charAt(i - 1))
          && Character.isUpperCase(name.charAt(i - 2))) {
        normalizedName += "_";
      }
      normalizedName += name.charAt(i);
    }
    return normalizedName.toLowerCase();
  }
}
