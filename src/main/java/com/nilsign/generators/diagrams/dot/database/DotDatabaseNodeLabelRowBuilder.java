package com.nilsign.generators.diagrams.dot.database;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.dxd.model.DxdFieldType;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.database.Sql;
import com.nilsign.generators.diagrams.dot.Dot;
import com.nilsign.misc.Pair;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotDatabaseNodeLabelRowBuilder {

  private static final List<String> TABLE_NODE_COLUMN_NAMES = Arrays.asList(
      "NAME", "TYPE", "INDEX", "UNIQUE", "NULLABLE", "FTS", "DEFAULT");

  private static final List<String> TABLE_RELATION_NODE_COLUMN_NAMES = Arrays.asList(
      "FK_1", "FK_2");

  private static final String YES = "yes";
  private static final String NO = "no";

  public static String buildTableNodeNameRow(@NonNull DxdClass aClass) {
    return Dot.addNodeLabelTableName(
        Sql.buildTableName(aClass),
        TABLE_NODE_COLUMN_NAMES.size());
  }

  public static String buildRelationalTableNodeNameRow(@NonNull DxdFieldRelation relation) {
    return Dot.addNodeLabelTableName(
        Sql.buildTableName(relation),
        TABLE_RELATION_NODE_COLUMN_NAMES.size());
  }

  public static String buildTableNodeColumnNamesRow() {
    return Dot.addNodeLabelTableColumnNames(TABLE_NODE_COLUMN_NAMES);
  }

  public static String buildRelationalTableNodeColumnNamesRow() {
    return Dot.addNodeLabelTableColumnNames(TABLE_RELATION_NODE_COLUMN_NAMES);
  }

  public static String buildTableNodeFieldRow(
      @NonNull DxdModel model,
      @NonNull DxdField field) {
    StringBuffer output = new StringBuffer()
        .append(Dot.openNodeLabelTableRow());
    for (int i = 0; i < TABLE_NODE_COLUMN_NAMES.size(); ++i) {
      output.append(field.hasRelation()
          && i == TABLE_NODE_COLUMN_NAMES.size() - 1
          && hasForeignKeyPorts(model)
          ? Dot.addNodeLabelTableCell(getCellValue(i, field), String.format("port_%s",
          Sql.buildForeignKeyFieldName(!field.getRelationType().isManyToOne()
              ? field.getType().getObjectName()
              : field.getName())))
          : Dot.addNodeLabelTableCell(getCellValue(i, field)));
    }
    return output
        .append(Dot.closeNodeLabelTableRow())
        .toString();
  }

  public static String buildTableNodePrimaryKeyRow(@NonNull DxdModel model) {
    return new StringBuffer()
        .append(Dot.openNodeLabelTableRow())
        .append(hasPrimaryKeyPorts(model)
            ? Dot.addNodeLabelTableCell(
                Sql.SQL_PRIMARY_KEY_NAME,
                String.format("port_%s", Sql.SQL_PRIMARY_KEY_NAME))
            : Dot.addNodeLabelTableCell(Sql.SQL_PRIMARY_KEY_NAME))
        .append(Dot.addNodeLabelTableCell(DxdFieldType.LONG_TYPE_NAME))
        .append(Dot.addNodeLabelTableCell(YES))
        .append(Dot.addNodeLabelTableCell(NO))
        .append(Dot.addNodeLabelTableCell(NO))
        .append(Dot.addNodeLabelTableCell(NO))
        .append(Dot.addNodeLabelTableCell(NO))
        .append(Dot.closeNodeLabelTableRow())
        .toString();
  }

  public static String buildRelationalTableNodeFieldsRow(
      @NonNull DxdModel model,
      @NonNull DxdFieldRelation relation) {
    Pair<String, String> foreignKeyName = Sql.buildForeignKeyFieldNames(relation);
    return new StringBuffer()
        .append(Dot.openNodeLabelTableRow())
        .append(hasForeignKeyPorts(model)
            ? Dot.addNodeLabelTableCell(
                  foreignKeyName.getFirst(),
                  String.format("port_%s", foreignKeyName.getFirst()))
            : Dot.addNodeLabelTableCell(foreignKeyName.getFirst()))
        .append(hasForeignKeyPorts(model)
            ? Dot.addNodeLabelTableCell(
                foreignKeyName.getSecond(),
                String.format("port_%s", foreignKeyName.getSecond()))
            : Dot.addNodeLabelTableCell(foreignKeyName.getSecond()))
        .append(Dot.closeNodeLabelTableRow())
        .toString();
  }

  private static String getCellValue(@NonNull int index, @NonNull DxdField field) {
    switch (index) {
      case 0: return getFieldNameCellValue(field);
      case 1: return getFieldTypeCellValue(field);
      case 2: return getFieldIndexCellValue(field);
      case 3: return getFieldUniqueCellValue(field);
      case 4: return getFieldNullableCellValue(field);
      case 5: return getFieldFtsCellValue(field);
      case 6: return getFieldDefaultCellValue(field);
      default: return null;
    }
  }

  private static String getFieldNameCellValue(@NonNull DxdField field) {
    return field.hasRelation()
        ? Sql.buildForeignKeyFieldName(field.getType().getObjectName())
        : Sql.buildFieldName(field);
  }

  private static String getFieldTypeCellValue(@NonNull DxdField field) {
    return field.hasRelation()
        ? field.getType().getObjectName().toLowerCase()
        : DxdFieldType.LONG_TYPE_NAME.toLowerCase();
  }

  private static String getFieldIndexCellValue(@NonNull DxdField field) {
    return field.hasRelation()
        || field.isIndexed()
        || field.isUnique()
        || field.isFts() ? YES : NO;
  }

  private static String getFieldUniqueCellValue(@NonNull DxdField field) {
    return field.isUnique()
        || field.hasRelation()
        && field.getRelationType().isOneToOne()
        && !field.isNullable() ? YES : NO;
  }

  private static String getFieldNullableCellValue(@NonNull DxdField field) {
    return field.isNullable() ? YES : NO;
  }

  private static String getFieldFtsCellValue(@NonNull DxdField model) {
    return model.isFts() ? YES : NO;
  }

  private static String getFieldDefaultCellValue(@NonNull DxdField field) {
    if (field.hasRelation()
        || field.getType().isBlob()
        || field.getType().isDate()) {
      return NO;
    }
    if (field.getDefaultValue() == null) {
      return field.isNullable() ? "null" : NO;
    }
    if (!field.getType().isString()) {
      return field.getDefaultValue();
    }
    return field.getDefaultValue().length() <= 10
        ? String.format("'%s'", field.getDefaultValue())
        : String.format("'%s...'", field.getDefaultValue().substring(0, 8));
  }

  private static boolean hasPrimaryKeyPorts(@NonNull DxdModel model) {
    return model
        .getConfig()
        .isDiagramDatabasePrimaryKeyFieldPorts();
  }

  private static boolean hasForeignKeyPorts(@NonNull DxdModel model) {
    return model
        .getConfig()
        .isDiagramDatabaseForeignKeyFieldPorts();
  }
}
