package com.nilsign.generators.diagrams.database.dot;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.dxd.model.DxdFieldType;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.database.SqlSchemaGenerator;
import com.nilsign.misc.Pair;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotDatabaseNodeLabelRowBuilder {

  private static final List<String> ENTITY_NODE_COLUMN_NAMES = Arrays.asList(
      "NAME", "TYPE", "INDEX", "UNIQUE", "NULLABLE", "FTS", "DEFAULT");

  private static final List<String> ENTITY_RELATION_NODE_COLUMN_NAMES = Arrays.asList(
      "FK_1", "FK_2");

  private static final String YES = "yes";
  private static final String NO = "no";

  public static String buildEntityNodeNameRow(@NonNull DxdClass dxdClass) {
    return Dot.addNodeLabelTableName(
        SqlSchemaGenerator.buildTableName(dxdClass),
        ENTITY_NODE_COLUMN_NAMES.size());
  }

  public static String buildEntityRelationNodeNameRow(@NonNull DxdFieldRelation dxdRelation) {
    return Dot.addNodeLabelTableName(
        SqlSchemaGenerator.buildTableName(dxdRelation),
        ENTITY_RELATION_NODE_COLUMN_NAMES.size());
  }

  public static String buildEntityNodeColumnNamesRow() {
    return Dot.addNodeLabelTableColumnNames(ENTITY_NODE_COLUMN_NAMES);
  }

  public static String buildEntityRelationNodeColumnNamesRow() {
    return Dot.addNodeLabelTableColumnNames(ENTITY_RELATION_NODE_COLUMN_NAMES);
  }

  public static String buildEntityNodeFieldRow(
      @NonNull DxdModel dxdModel,
      @NonNull DxdField dxdField) {
    StringBuffer output = new StringBuffer()
        .append(Dot.openNodeLabelTableRow());
    for (int i = 0; i < ENTITY_NODE_COLUMN_NAMES.size(); ++i) {
      output.append(dxdField.hasRelation()
          && i == ENTITY_NODE_COLUMN_NAMES.size() - 1
          && hasForeignKeyPorts(dxdModel)
          ? Dot.addNodeLabelTableCell(getCellValue(i, dxdField), String.format("port_%s",
          SqlSchemaGenerator.buildForeignKeyName(!dxdField.getRelationType().isManyToOne()
              ? dxdField.getType().getObjectName()
              : dxdField.getName())))
          : Dot.addNodeLabelTableCell(getCellValue(i, dxdField)));
    }
    return output
        .append(Dot.closeNodeLabelTableRow())
        .toString();
  }

  public static String buildEntityNodePrimaryKeyRow(@NonNull DxdModel dxdModel) {
    return new StringBuffer()
        .append(Dot.openNodeLabelTableRow())
        .append(hasPrimaryKeyPorts(dxdModel)
            ? Dot.addNodeLabelTableCell(
            SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME,
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME))
            : Dot.addNodeLabelTableCell(SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME))
        .append(Dot.addNodeLabelTableCell(DxdFieldType.LONG_TYPE_NAME))
        .append(Dot.addNodeLabelTableCell(YES))
        .append(Dot.addNodeLabelTableCell(NO))
        .append(Dot.addNodeLabelTableCell(NO))
        .append(Dot.addNodeLabelTableCell(NO))
        .append(Dot.addNodeLabelTableCell(NO))
        .append(Dot.closeNodeLabelTableRow())
        .toString();
  }

  public static String buildEntityRelationNodeFieldsRow(
      @NonNull DxdModel dxdModel,
      @NonNull DxdFieldRelation dxdRelation) {
    Pair<String, String> foreignKeyName = SqlSchemaGenerator.buildForeignKeyNames(dxdRelation);
    return new StringBuffer()
        .append(Dot.openNodeLabelTableRow())
        .append(hasForeignKeyPorts(dxdModel)
            ? Dot.addNodeLabelTableCell(
                  foreignKeyName.getFirst(),
                  String.format("port_%s", foreignKeyName.getFirst()))
            : Dot.addNodeLabelTableCell(foreignKeyName.getFirst()))
        .append(hasForeignKeyPorts(dxdModel)
            ? Dot.addNodeLabelTableCell(
                foreignKeyName.getSecond(),
                String.format("port_%s", foreignKeyName.getSecond()))
            : Dot.addNodeLabelTableCell(foreignKeyName.getSecond()))
        .append(Dot.closeNodeLabelTableRow())
        .toString();
  }

  private static String getCellValue(@NonNull int index, @NonNull DxdField dxdField) {
    switch (index) {
      case 0: return getFieldNameCellValue(dxdField);
      case 1: return getFieldTypeCellValue(dxdField);
      case 2: return getFieldIndexCellValue(dxdField);
      case 3: return getFieldUniqueCellValue(dxdField);
      case 4: return getFieldNullableCellValue(dxdField);
      case 5: return getFieldFtsCellValue(dxdField);
      case 6: return getFieldDefaultCellValue(dxdField);
      default: return null;
    }
  }

  private static String getFieldNameCellValue(@NonNull DxdField dxdField) {
    return dxdField.hasRelation()
        ? SqlSchemaGenerator.buildForeignKeyName(dxdField.getType().getObjectName())
        : SqlSchemaGenerator.buildFieldName(dxdField);
  }

  private static String getFieldTypeCellValue(@NonNull DxdField dxdField) {
    return dxdField.hasRelation()
        ? dxdField.getType().getObjectName().toLowerCase()
        : DxdFieldType.LONG_TYPE_NAME.toLowerCase();
  }

  private static String getFieldIndexCellValue(@NonNull DxdField dxdField) {
    return dxdField.hasRelation()
        || dxdField.isIndexed()
        || dxdField.isUnique()
        || dxdField.isFts() ? YES : NO;
  }

  private static String getFieldUniqueCellValue(@NonNull DxdField dxdField) {
    return dxdField.isUnique()
        || dxdField.hasRelation()
        && dxdField.getRelationType().isOneToOne()
        && !dxdField.isNullable() ? YES : NO;
  }

  private static String getFieldNullableCellValue(@NonNull DxdField dxdField) {
    return dxdField.isNullable() ? YES : NO;
  }

  private static String getFieldFtsCellValue(@NonNull DxdField dxdField) {
    return dxdField.isFts() ? YES : NO;
  }

  private static String getFieldDefaultCellValue(@NonNull DxdField dxdField) {
    if (dxdField.hasRelation()
        || dxdField.getType().isBlob()
        || dxdField.getType().isDate()) {
      return NO;
    }
    if (dxdField.getDefaultValue() == null) {
      return dxdField.isNullable() ? "null" : NO;
    }
    if (!dxdField.getType().isString()) {
      return dxdField.getDefaultValue();
    }
    return dxdField.getDefaultValue().length() <= 10
        ? String.format("'%s'", dxdField.getDefaultValue())
        : String.format("'%s...'", dxdField.getDefaultValue().substring(0, 8));
  }

  private static boolean hasPrimaryKeyPorts(@NonNull DxdModel dxdModel) {
    return dxdModel
        .getConfig()
        .isDiagramDatabasePrimaryKeyFieldPorts();
  }

  private static boolean hasForeignKeyPorts(@NonNull DxdModel dxdModel) {
    return dxdModel
        .getConfig()
        .isDiagramDatabaseForeignKeyFieldPorts();
  }
}
