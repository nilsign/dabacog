package com.nilsign.generators.graphs;

import com.google.common.collect.Lists;
import com.nilsign.dxd.elements.DxdModel;
import com.nilsign.dxd.elements.entities.DxdEntityClass;
import com.nilsign.dxd.elements.entities.DxdEntityClassField;
import com.nilsign.generators.sql.SqlSchemaGenerator;
import com.nilsign.misc.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseGraphGenerator extends GraphGenerator  {

  private static final String GRAPHVIZ_DOT_FILE_NAME = "database-entity-relation-diagram.pot";

  public static void run(DxdModel model) throws GraphGeneratorException {
    new DatabaseGraphGenerator(model).generate();
  }

  private DatabaseGraphGenerator(DxdModel dxdModel) {
    super(dxdModel);
  }

  @Override
  protected String getOutputDirectory() {
    return dxdModel.getDxdMeta().getDxdMetaDiagrams().getOutputPath();
  }

  @Override
  protected String getTargetFileName() {
    return GRAPHVIZ_DOT_FILE_NAME;
  }

  @Override
  public void generate() throws GraphGeneratorException {
    File outputFile = super.createTargetFile();
    try (FileWriter writer = new FileWriter(outputFile)) {
      writer.write(new StringBuffer()
          .append(openGraphml())
          .append(addGraphmlMetaDescription("Database Description"))
          .append(addDatabaseTables())
          .append(addDatabaseTableRelations())
          .append(closeGraphmlGraph())
          .toString());
    } catch (IOException e) {
      throw new GraphGeneratorException(
          "An error occurred while writing to the database diagram description file.", e);
    }
  }

  private String addDatabaseTables() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getEntities().getDxdClasses().forEach(dxdEntityClass -> {
      output.append(addDatabaseTable(dxdEntityClass));
    });
    output.append(addDatabaseManyToManyTables());
    return output.toString();
  }

  private String addDatabaseTable(DxdEntityClass dxdClass) {
    return new StringBuffer()
        .append(openGraphmlNode(SqlSchemaGenerator.buildTableName(dxdClass)))
        .append(openGraphmlLabel())
        .append(openGraphmlTable())
        .append(addGraphmlTableName(SqlSchemaGenerator.buildTableName(dxdClass), 5))
        .append(addGraphmlTableColumnNames(List.of("NAME", "TYPE", "INDEX", "UNIQUE", "NULLABLE")))
        .append(addGraphmlTableRows(getDatabaseTableColumnValues(dxdClass.getFields())))
        .append(closeGraphmlTable())
        .append(closeGraphmlLabel())
        .append(closeGraphmlNode())
        .toString();
  }

  private String addDatabaseManyToManyTables() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getEntities().getDistinctManyToManyClassRelationsList()
        .forEach(relation -> output.append(addDatabaseManyToManyTable(relation)));
    return output.toString();
  }

  private String addDatabaseManyToManyTable(Pair<DxdEntityClass, DxdEntityClass> relation) {
    String tableName = SqlSchemaGenerator.buildManyToManyTableName(relation);
    return new StringBuffer()
        .append(openGraphmlNode(tableName))
        .append(openGraphmlLabel())
        .append(openGraphmlTable())
        .append(addGraphmlTableName(tableName, 2))
        .append(addGraphmlTableColumnNames(List.of("FK_1", "FK_2")))
        .append(addGraphmlTableRows(getDatabaseManyToManyTableColumnValues(relation)))
        .append(closeGraphmlTable())
        .append(closeGraphmlLabel())
        .append(closeGraphmlNode())
        .toString();
  }

  private List<List<String>> getDatabaseTableColumnValues(List<DxdEntityClassField> fields) {
    List<List<String>> tableValues = new ArrayList<>();
    fields.forEach(field -> {
      tableValues.add(Lists.newArrayList(
            field.isRelation()
                ? SqlSchemaGenerator.buildForeignKeyName(field.getRefersTo())
                : field.getName(),
            field.getType(),
            "todo",
            "todo",
            "todo"));
    });
    return tableValues;
  }

  private List<List<String>> getDatabaseManyToManyTableColumnValues(
      Pair<DxdEntityClass, DxdEntityClass> relation) {
    return List.of(List.of(
        SqlSchemaGenerator.buildForeignKeyName(relation.getFirst().getName()),
        SqlSchemaGenerator.buildForeignKeyName(relation.getSecond().getName())));
  }

  private String addDatabaseTableRelations() {
    StringBuffer output = new StringBuffer();
    // Many-to-many edges
    super.dxdModel.getEntities().getDistinctManyToManyClassRelationsList().forEach(relation
        -> output
            .append(String.format(
                "\tgml_node_%s -> gml_node_%s;\n",
                SqlSchemaGenerator.buildManyToManyTableName(relation),
                SqlSchemaGenerator.buildTableName(relation.getFirst())))
            .append(String.format(
                "\tgml_node_%s -> gml_node_%s;\n",
                SqlSchemaGenerator.buildManyToManyTableName(relation),
                SqlSchemaGenerator.buildTableName(relation.getSecond()))));
    // Many-to-one edges
    super.dxdModel.getEntities().getDistinctManyToOneClassRelationsList().forEach(relation
        -> output
            .append(String.format(
                "\tgml_node_%s -> gml_node_%s [style=\"dashed\"];\n",
                SqlSchemaGenerator.buildTableName(relation.getFirst()),
                SqlSchemaGenerator.buildTableName(relation.getSecond()))));
    // One-to-many edges
    super.dxdModel.getEntities().getDistinctOneToManyClassRelationsList().forEach(relation
        -> output
            .append(String.format(
                "\tgml_node_%s -> gml_node_%s [style=\"dashed\"];\n",
                SqlSchemaGenerator.buildTableName(relation.getSecond()),
                SqlSchemaGenerator.buildTableName(relation.getFirst()))));
    // One-to-one edges
    super.dxdModel.getEntities().getDistinctOneToOneClassRelationsList().forEach(relation
        -> output
            .append(String.format(
                "\tgml_node_%s -> gml_node_%s [style=\"dotted\"];\n",
                SqlSchemaGenerator.buildTableName(relation.getFirst()),
                SqlSchemaGenerator.buildTableName(relation.getSecond())))
            .append(String.format(
                "\tgml_node_%s -> gml_node_%s [style=\"dotted\"];\n",
                SqlSchemaGenerator.buildTableName(relation.getSecond()),
                SqlSchemaGenerator.buildTableName(relation.getFirst()))));
    return output.toString();
  }
}
