package com.nilsign.generators.database.postgresql;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.generators.database.Sql;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PostgreSqlForeignKeyBuilder {

  public static String buildForeignKeys(@NonNull DxdFieldRelation relation) {
    return relation.getType().isManyToMany()
        ? new StringBuffer()
            .append(buildForeignKey(
                Sql.buildTableName(relation),
                Sql.buildConstraintsNameForRelationalTableForeignKeyField(
                    relation,
                    relation.getFirstClass()),
                Sql.buildForeignKeyFieldName(relation.getFirstClass().getName()),
                Sql.buildTableName(relation.getFirstClass())))
            .append(buildForeignKey(
                Sql.buildTableName(relation),
                Sql.buildConstraintsNameForRelationalTableForeignKeyField(
                    relation,
                    relation.getSecondClass()),
                Sql.buildForeignKeyFieldName(relation.getSecondClass().getName()),
                Sql.buildTableName(relation.getSecondClass())))
            .toString()
        : "";
  }

  public static String buildForeignKey(
      @NonNull DxdClass aClass,
      @NonNull DxdField field) {
    return field.hasRelation() && !field.getRelationType().isManyToMany()
        ? buildForeignKey(
            Sql.buildTableName(aClass),
            Sql.buildConstraintsNameForForeignKeyField(aClass, field),
            Sql.buildForeignKeyFieldName(field.getName()),
            Sql.buildTableName(field.getName()))
        : "";
  }

  private static String buildForeignKey(
      @NonNull String referencingTableName,
      @NonNull String constraintName,
      @NonNull String foreignKeyName,
      @NonNull String referencedTableName) {
    return new StringBuffer()
        .append(String.format(
            "\nALTER TABLE %s",
            referencingTableName))
        .append(String.format(
            "\n    ADD CONSTRAINT %s",
            constraintName))
        .append(String.format(
            "\n    FOREIGN KEY(%s)",
            foreignKeyName))
        .append(String.format(
            "\n    REFERENCES %s(%s)",
            referencedTableName,
            Sql.SQL_PRIMARY_KEY_NAME))
        .append("\n    ON UPDATE NO ACTION")
        .append("\n    ON DELETE NO ACTION;\n")
        .toString();
  }
}
