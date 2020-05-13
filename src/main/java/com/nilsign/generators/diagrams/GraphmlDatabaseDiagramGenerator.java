package com.nilsign.generators.diagrams;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.dxd.xml.entities.DxdEntityField;
import com.nilsign.dxd.xmlvaluetypes.DxdAttributeType;
import com.nilsign.generators.sql.SqlSchemaGenerator;
import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphmlDatabaseDiagramGenerator extends GraphmlGenerator {

  public static final String TARGET_FILE_NAME = "dabacog-db-diagram.pot";

  public static void run(@NonNull DxdModel model) throws GraphmlGeneratorException {
    new GraphmlDatabaseDiagramGenerator(model).generate();
  }

  private GraphmlDatabaseDiagramGenerator(@NonNull DxdModel dxdModel) {
    super(dxdModel);
  }

  @Override
  protected String getOutputDirectory() {
    return dxdModel.getMeta().getMetaDiagrams().getOutputPath();
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

  private List<List<String>> getDatabaseTableColumnValues(@NonNull List<DxdEntityField> fields) {
    List<List<String>> tableValues = new ArrayList<>();
    tableValues.add(Arrays.asList(
        SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME,
        DxdAttributeType.LONG.toString(),
        "YES",
        "YES",
        "NO"));
    fields.forEach(field -> {
      if (!field.isRelation()
          || !field.getRelation().isManyToMany()
          && !(field.isToManyRelation() && field.getRelation().isManyToOne())) {
        tableValues.add(Arrays.asList(
            field.isRelation()
                ? SqlSchemaGenerator.buildForeignKeyName(field.getRefersTo())
                : field.getName(),
            field.getType(),
            "todo",
            "todo",
            "todo"));
        }
    });
    return tableValues;
  }

  private String addDatabaseManyToManyTables() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getEntities().getManyToManyRelations().forEach(relation -> {
      String tableName = SqlSchemaGenerator.buildManyToManyTableName(relation);
      output
          .append(openGraphmlNode(tableName))
          .append(openGraphmlLabel())
          .append(openGraphmlTable())
          .append(addGraphmlTableName(tableName, 2))
          .append(addGraphmlTableColumnNames(Arrays.asList("FK_1", "FK_2")))
          .append(addGraphmlTableRows(getDatabaseManyToManyTableColumnValues(relation)))
          .append(closeGraphmlTable())
          .append(closeGraphmlLabel())
          .append(closeGraphmlNode());
    });
    return output.toString();
  }

  private List<List<String>> getDatabaseManyToManyTableColumnValues(
      @NonNull DxdEntityRelation relation) {
    return Arrays.asList(
        Arrays.asList(
            SqlSchemaGenerator.buildForeignKeyName(relation.getReferencingClass().getName()),
            SqlSchemaGenerator.buildForeignKeyName(relation.getReferencedClass().getName()))
    );
  }

  private String addDatabaseTableRelations() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getEntities().getRelations().forEach(relation -> {
      switch(relation.getType()) {
        case MANY_TO_MANY:
          output.append(String.format("\tgml_node_%s -> gml_node_%s;\n",
              SqlSchemaGenerator.buildManyToManyTableName(relation),
              SqlSchemaGenerator.buildTableName(relation.getReferencingClass())));
          output.append(String.format("\tgml_node_%s -> gml_node_%s;\n",
              SqlSchemaGenerator.buildManyToManyTableName(relation),
              SqlSchemaGenerator.buildTableName(relation.getReferencedClass())));
          break;
        case MANY_TO_ONE:
          output.append(String.format("\tgml_node_%s -> gml_node_%s [style=\"dashed\"];\n",
              SqlSchemaGenerator.buildTableName(relation.getReferencingClass()),
              SqlSchemaGenerator.buildTableName(relation.getReferencedClass())));
          break;
        case ONE_TO_MANY:
          output.append(String.format("\tgml_node_%s -> gml_node_%s [style=\"dashed\"];\n",
              SqlSchemaGenerator.buildTableName(relation.getReferencedClass()),
              SqlSchemaGenerator.buildTableName(relation.getReferencingClass())));
          break;
        case ONE_TO_ONE:
          output.append(String.format("\tgml_node_%s -> gml_node_%s [style=\"dotted\"];\n",
              SqlSchemaGenerator.buildTableName(relation.getReferencingClass()),
              SqlSchemaGenerator.buildTableName(relation.getReferencedClass())));
          output.append(String.format("\tgml_node_%s -> gml_node_%s [style=\"dotted\"];\n",
                  SqlSchemaGenerator.buildTableName(relation.getReferencedClass()),
                  SqlSchemaGenerator.buildTableName(relation.getReferencingClass())));
          break;
      }
    });
    return output.toString();
  }
}
