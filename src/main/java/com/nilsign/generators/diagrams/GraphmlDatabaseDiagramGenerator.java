package com.nilsign.generators.diagrams;

import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.dxd.xml.entities.DxdEntityField;
import com.nilsign.generators.sql.SqlSchemaGenerator;
import com.nilsign.misc.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphmlDatabaseDiagramGenerator extends GraphmlGenerator {

  public static final String TARGET_FILE_NAME = "dabacog-db-diagram.pot";

  public static void run(DxdModel model) throws GraphmlGeneratorException {
    new GraphmlDatabaseDiagramGenerator(model).generate();
  }

  private GraphmlDatabaseDiagramGenerator(DxdModel dxdModel) {
    super(dxdModel);
  }

  @Override
  protected String getOutputDirectory() {
    return dxdModel.getDxdMeta().getDxdMetaDiagrams().getOutputPath();
  }

  @Override
  protected String getTargetFileName() {
    return TARGET_FILE_NAME;
  }

  public void generate() throws GraphmlGeneratorException {
    File outputFile = super.createGenerationTargetFile();
    try (FileWriter writer = new FileWriter(outputFile)) {
      writer.write(new StringBuffer()
          .append(openGraphml())
          .append(addGraphmlMetaDescription("Database Description"))
          .append(addDatabaseTables())
          .append(addDatabaseTableRelations())
          .append(closeGraphmlGraph())
          .toString());
    } catch (IOException e) {
      throw new GraphmlGeneratorException(
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
        .append(addGraphmlTableColumnNames(Arrays.asList(
            "NAME", "TYPE", "INDEX", "UNIQUE", "NULLABLE")))
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
        .append(addGraphmlTableColumnNames(Arrays.asList("FK_1", "FK_2")))
        .append(addGraphmlTableRows(getDatabaseManyToManyTableColumnValues(relation)))
        .append(closeGraphmlTable())
        .append(closeGraphmlLabel())
        .append(closeGraphmlNode())
        .toString();
  }

  private List<List<String>> getDatabaseTableColumnValues(List<DxdEntityField> fields) {
    List<List<String>> tableValues = new ArrayList<>();
    fields.forEach(field -> {
      tableValues.add(Arrays.asList(
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
    return Arrays.asList(
        Arrays.asList(
            SqlSchemaGenerator.buildForeignKeyName(relation.getFirst().getName()),
            SqlSchemaGenerator.buildForeignKeyName(relation.getSecond().getName()))
    );
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
