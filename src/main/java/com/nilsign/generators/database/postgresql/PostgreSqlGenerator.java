package com.nilsign.generators.database.postgresql;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.GeneratedFilePaths;
import com.nilsign.generators.Generator;
import com.nilsign.generators.database.Sql;
import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;

public final class PostgreSqlGenerator extends Generator {

  public static final String OUTPUT_FILE_NAME = "InitializeDatabase.sql";

  private PostgreSqlGenerator(@NonNull DxdModel model) {
    super(model);
  }

  private static PostgreSqlGenerator of(@NonNull DxdModel model) {
    return new PostgreSqlGenerator(model);
  }

  public static void run(@NonNull DxdModel model) {
    try {
      PostgreSqlGenerator.of(model).generate();
    } catch (Exception e) {
      throw new PostgreSqlGeneratorException(e);
    }
  }

  @Override
  protected String getOutputDirectory() {
    return super.model.getConfig().getDiagramDatabaseOutputPath();
  }

  @Override
  protected String getOutputFileName() {
    return OUTPUT_FILE_NAME;
  }

  private void generate() {
    File outputFile = super.createOutputFile();
    try (FileWriter writer = new FileWriter(outputFile)) {
      writer.write(
         new StringBuffer()
            .append(buildSetup())
            .append(buildTables())
            .toString());

    } catch (Exception e) {
      throw new RuntimeException(
        String.format(
            "Failed to write into target file '%s'.",
            outputFile),
        e);
    }
    GeneratedFilePaths.setSqlScriptFile(outputFile.getAbsolutePath());
  }

  private String buildSetup() {
    StringBuffer output = new StringBuffer()
        .append("-- Global Setup.\n")
        .append("SET client_encoding = 'UTF8';\n\n");
    if (model.getConfig().isSqlGlobalSequence()) {
       output
           .append("-- Configures a global id sequence shared by all generated primary keys.\n")
           .append("CREATE SEQUENCE public.shared_sequence\n")
           .append("    START WITH 1\n")
           .append("    INCREMENT BY 1\n")
           .append("    NO MINVALUE\n")
           .append("    NO MAXVALUE\n")
           .append("    CACHE 1;\n");
    }
    return output.toString();
  }

  private String buildTables() {
    StringBuffer output = new StringBuffer()
        .append("\n-- Creates normal tables and the required indices");
    super.model.getClasses().forEach(aClass -> {
        output.append(buildTable(aClass));
        output.append(buildTableForeignKeyIndices(aClass));
        output.append("\n");
    });
    return output.toString();
  }

  private String buildTable(@NonNull DxdClass aClass) {
    StringBuffer output = new StringBuffer()
        .append(String.format("\nCREATE TABLE IF NOT EXISTS %s (\n", Sql.buildTableName(aClass)))
        .append(String.format(
            "    %s BIGINT CONSTRAINT %s PRIMARY KEY",
            Sql.SQL_PRIMARY_KEY_NAME,
            Sql.buildConstraintsNameForPrimaryKeyField(aClass)));
        aClass.getFields().forEach(field -> output.append(buildField(field)));
        aClass.getFields().forEach(field -> output.append(buildConstraints(aClass, field)));
        output.append("\n);");
    return output.toString();
  }

  private String buildField(@NonNull DxdField field) {
    return String.format(
        ",\n    %s %s%s%s",
        Sql.buildFieldName(field),
        buildDataType(field),
        buildNotNull(field),
        buildDefaultValue(field));
  }

  private String buildDataType(@NonNull DxdField field) {
    String dataType =
        field.getType().isObject() ? "BIGINT" :
        field.getType().isString() ? "TEXT" :
        field.getType().isLong() ? "BIGINT" :
        field.getType().isInt() ? "INTEGER" :
        field.getType().isDouble() ? "NUMERIC" :
        field.getType().isFloat() ? "NUMERIC" :
        field.getType().isBoolean() ? "BOOLEAN" :
        field.getType().isDate() ? "TIMESTAMP" :
        field.getType().isBlob() ? "BYTEA" :
        null;
    if (dataType == null) {
      throw new RuntimeException(String.format(
          "Unsupported Sql datatype '%s'.",
          field.getType().getName()));
    }
    return dataType;
  }

  private String buildNotNull(@NonNull DxdField field) {
    return !field.isNullable()
        ? " NOT NULL"
        : "";
  }

  private String buildDefaultValue(@NonNull DxdField field) {
    return field.hasDefaultValue()
        ? String.format(" DEFAULT %s", field.getDefaultValue())
        : "";
  }

  private String buildConstraints(@NonNull DxdClass aClass, @NonNull DxdField field) {
    return new StringBuffer()
        .append(buildForeignKeyConstraint(aClass, field))
        .append(buildUniqueConstraint(aClass, field))
        .toString();
  }

  private String buildForeignKeyConstraint(
      @NonNull DxdClass aClass,
      @NonNull DxdField field) {
    return field.hasRelation()
        ? new StringBuffer()
            .append(String.format(
                ",\n    CONSTRAINT %s FOREIGN KEY(%s)",
                Sql.buildConstraintsNameForForeignKeyField(aClass, field),
                Sql.buildForeignKeyName(field.getName())))
            .append(String.format(
                "\n        REFERENCES tbl_address(%s)",
                Sql.SQL_PRIMARY_KEY_NAME))
            .append("\n        ON UPDATE NO ACTION")
            .append("\n        ON DELETE NO ACTION")
            .toString()
        : "";
  }

  private String buildUniqueConstraint(@NonNull DxdClass aClass, @NonNull DxdField field) {
    return field.isUnique()
        ? String.format(
            ",\n    CONSTRAINT %s UNIQUE",
            Sql.buildConstraintsNameForUniqueField(aClass, field))
        : "";
  }

  private String buildTableForeignKeyIndices(@NonNull DxdClass aClass) {
    StringBuffer output = new StringBuffer();
    aClass.getFields().forEach(field -> {
      if (field.hasRelation()) {
        output.append(
            String.format(
                "\nCREATE INDEX %s ON %s(%s);",
                Sql.buildIndexNameForForeignKeyField(aClass, field),
                Sql.buildTableName(aClass),
                Sql.buildForeignKeyName(field.getName())));
      }
    });
    return output.toString();
  }
}
