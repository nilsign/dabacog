package com.nilsign.generators.diagrams.database;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.dxd.xml.entities.DxdEntityField;
import com.nilsign.dxd.xmlvaluetypes.DxdFieldType;
import com.nilsign.generators.database.SqlSchemaGenerator;
import com.nilsign.generators.diagrams.Graphml;
import com.nilsign.misc.Pair;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public class GraphmlDatabaseNodeLabelRowBuilder {

  private static final List<String> ENTITY_NODE_COLUMN_NAMES = Arrays.asList(
      "NAME", "TYPE", "INDEX", "UNIQUE", "NULLABLE", "FTS", "DEFAULT");

  private static final List<String> ENTITY_RELATION_NODE_COLUMN_NAMES = Arrays.asList(
      "FK_1", "FK_2");

  private static final String YES = "yes";
  private static final String NO = "no";

  public static String buildEntityNodeNameRow(@NonNull DxdEntityClass dxdClass) {
    return Graphml.addNodeLabelTableName(
        SqlSchemaGenerator.buildTableName(dxdClass),
        ENTITY_NODE_COLUMN_NAMES.size());
  }

  public static String buildEntityRelationNodeNameRow(@NonNull DxdEntityRelation dxdRelation) {
    return Graphml.addNodeLabelTableName(
        SqlSchemaGenerator.buildTableName(dxdRelation),
        ENTITY_RELATION_NODE_COLUMN_NAMES.size());
  }

  public static String buildEntityNodeColumnNamesRow() {
    return Graphml.addNodeLabelTableColumnNames(ENTITY_NODE_COLUMN_NAMES);
  }

  public static String buildEntityRelationNodeColumnNamesRow() {
    return Graphml.addNodeLabelTableColumnNames(ENTITY_RELATION_NODE_COLUMN_NAMES);
  }

  public static String buildEntityNodePrimaryKeyRow(@NonNull DxdModel dxdModel) {
    return of().buildEntityNodePrimaryKeyRowImpl(dxdModel);
  }

  public static String buildEntityNodeFieldRow(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityField dxdField) {
    return of().buildEntityNodeFieldLabelRow(dxdModel, dxdField);
  }

  public static String buildEntityRelationNodeFieldsRow(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityRelation dxdRelation) {
    return of().buildEntityRelationNodeFieldsRowImpl(dxdModel, dxdRelation);
  }
  private String buildEntityNodePrimaryKeyRowImpl(@NonNull DxdModel dxdModel) {
    return new StringBuffer()
        .append(Graphml.openNodeLabelTableRow())
        .append(hasPrimaryKeyPorts(dxdModel)
            ? Graphml.addNodeLabelTableCell(
                SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME,
                String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME))
            : Graphml.addNodeLabelTableCell(SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME))
        .append(Graphml.addNodeLabelTableCell(YES))
        .append(Graphml.addNodeLabelTableCell(YES))
        .append(Graphml.addNodeLabelTableCell(NO))
        .append(Graphml.addNodeLabelTableCell(NO))
        .append(Graphml.addNodeLabelTableCell(NO))
        .append(Graphml.addNodeLabelTableCell(NO))
        .append(Graphml.closeNodeLabelTableRow())
        .toString();
  }

  private String buildEntityNodeFieldLabelRow(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityField dxdField) {
    StringBuffer output = new StringBuffer()
        .append(Graphml.openNodeLabelTableRow());
    for (int i = 0; i < ENTITY_NODE_COLUMN_NAMES.size(); ++i) {
      output.append(dxdField.isRelation()
          && i == ENTITY_NODE_COLUMN_NAMES.size() - 1
          && hasForeignKeyPorts(dxdModel)
              ? Graphml.addNodeLabelTableCell(
                  getCellValue(i, dxdField),
                  String.format("port_%s_%s",
                      SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME,
                      dxdField.getRefersTo()))
              : Graphml.addNodeLabelTableCell(getCellValue(i, dxdField)));
    }
    return output
        .append(Graphml.closeNodeLabelTableRow())
        .toString();
  }

  private String buildEntityRelationNodeFieldsRowImpl(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityRelation dxdRelation) {
    Pair<String, String> foreignKeyName = SqlSchemaGenerator.buildForeignKeyNames(dxdRelation);
    return new StringBuffer()
        .append(Graphml.openNodeLabelTableRow())
        .append(hasForeignKeyPorts(dxdModel)
            ? Graphml.addNodeLabelTableCell(
                  foreignKeyName.getFirst(),
                  String.format("port_%s", foreignKeyName.getFirst()))
            : Graphml.addNodeLabelTableCell(foreignKeyName.getFirst()))
        .append(hasForeignKeyPorts(dxdModel)
            ? Graphml.addNodeLabelTableCell(
                foreignKeyName.getSecond(),
                String.format("port_%s", foreignKeyName.getSecond()))
            : Graphml.addNodeLabelTableCell(foreignKeyName.getSecond()))
        .append(Graphml.closeNodeLabelTableRow())
        .toString();
  }

  private String getCellValue(@NonNull int index, @NonNull DxdEntityField dxdField) {
    switch (index) {
      case 0: return getFieldNameCellValue(dxdField);
      case 1: return getFieldTypeCellValue(dxdField);
      case 2: return getFieldIndexCellValue(dxdField);
      case 3: return getFieldUniqueCellValue(dxdField);
      case 4: return getFieldNullableCellValue(dxdField);
      case 5: return getFieldFtsCellValue(dxdField);
      case 6: return getFieldDefaultCellValue(dxdField);
    }
    return null;
  }

  private String getFieldNameCellValue(@NonNull DxdEntityField dxdField) {
    return dxdField.isRelation()
        ? SqlSchemaGenerator.buildForeignKeyName(dxdField.getRefersTo())
        : dxdField.getName();
  }

  private String getFieldTypeCellValue(@NonNull DxdEntityField dxdField) {
    return (dxdField.isRelation()
        ? DxdFieldType.LONG.toString()
        : dxdField.getType().toString())
        .toLowerCase();
  }

  private String getFieldIndexCellValue(@NonNull DxdEntityField dxdField) {
    return dxdField.isRelation()
        || dxdField.isIndexed()
        || dxdField.isUnique()
        || dxdField.isFts() ? YES : NO;
  }

  private String getFieldUniqueCellValue(@NonNull DxdEntityField dxdField) {
    return dxdField.isUnique()
        || dxdField.isRelation()
        && dxdField.getRelation().isOneToOne()
        && !dxdField.isNullable() ? YES : NO;
  }

  private String getFieldNullableCellValue(@NonNull DxdEntityField dxdField) {
    return dxdField.isNullable() ? YES : NO;
  }

  private String getFieldFtsCellValue(@NonNull DxdEntityField dxdField) {
    return dxdField.isFts() ? YES : NO;
  }

  private String getFieldDefaultCellValue(@NonNull DxdEntityField dxdField) {
    if (dxdField.isRelation()
        || dxdField.getType() == DxdFieldType.BLOB
        || dxdField.getType() == DxdFieldType.DATE) {
      return NO;
    }
    if (dxdField.getDefaultValue() == null) {
      return dxdField.isNullable() ? "null" : NO;
    }
    if (dxdField.getType() != DxdFieldType.STRING) {
      return dxdField.getDefaultValue();
    }
    return dxdField.getDefaultValue().length() <= 10
        ? String.format("'%s'", dxdField.getDefaultValue())
        : String.format("'%s...'", dxdField.getDefaultValue().substring(0, 8));
  }

  private boolean hasPrimaryKeyPorts(@NonNull DxdModel dxdModel) {
    return dxdModel
        .getMeta()
        .getMetaDiagrams()
        .getDxdMetaDiagramsDatabase()
        .isPrimaryKeyFieldPorts();
  }

  private boolean hasForeignKeyPorts(@NonNull DxdModel dxdModel) {
    return dxdModel
        .getMeta()
        .getMetaDiagrams()
        .getDxdMetaDiagramsDatabase()
        .isForeignKeyFieldPorts();
  }
}
