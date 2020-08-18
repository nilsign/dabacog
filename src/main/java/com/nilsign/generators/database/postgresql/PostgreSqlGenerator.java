package com.nilsign.generators.database.postgresql;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.GeneratedFilePaths;
import com.nilsign.generators.Generator;
import com.nilsign.generators.database.Sql;
import com.nilsign.misc.Pair;
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
    if (super.model.getConfig().isSqlGlobalSequence()) {
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
        .append("\n-- Creates normal tables and the according indices");
    super.model.getClasses().forEach(aClass -> {
        output
            .append(buildTable(aClass))
            .append(buildTableForeignKeyIndices(aClass))
            .append("\n");
    });
    if (super.model.getDistinctManyToManyRelations().size() > 0) {
      output.append("\n-- Creates relational tables (n..n) and the according indices");
      super.model.getDistinctManyToManyRelations().forEach(relation -> {
        output
            .append(buildRelationalTable(relation))
            .append(buildRelationalTableForeignKeyIndices(relation))
            .append("\n");
      });
    }

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
                "\n        REFERENCES %s(%s)",
                Sql.buildTableName(field.getName()),
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

  private String buildRelationalTable(@NonNull DxdFieldRelation relation) {
    Pair<String, String> foreignKeyNames = Sql.buildForeignKeyNames(relation);
    return new StringBuffer()
        .append(String.format(
            "\nCREATE TABLE IF NOT EXISTS %s_%s (",
            Sql.buildTableName(relation.getFirstClass()),
            Sql.buildTableName(relation.getSecondClass())))
        .append(String.format(
            "\n    %s BIGINT NOT NULL",
            foreignKeyNames.getFirst()))
        .append(String.format(
            "\n    %s BIGINT NOT NULL",
            foreignKeyNames.getSecond()))
        .append(String.format(
            "\n    CONSTRAINT %s PRIMARY KEY (%s, %s)",
            Sql.buildConstraintsNameForRelationalTablePrimaryKeyFields(relation),
            foreignKeyNames.getFirst(),
            foreignKeyNames.getSecond()))
        .append(buildRelationalTableForeignKeyConstraints(relation))
        .append("\n);")
        .toString();
  }

  private String buildRelationalTableForeignKeyConstraints(@NonNull DxdFieldRelation relation) {
    return new StringBuffer()
        .append(String.format(
            ",\n    CONSTRAINT %s FOREIGN KEY(%s)",
            Sql.buildConstraintsNameForRelationalTableForeignKeyField(
                relation,
                relation.getFirstClass()),
            Sql.buildForeignKeyName(relation.getFirstClass().getName())))
        .append(String.format(
            "\n        REFERENCES %s(%s)",
            Sql.buildTableName(relation.getFirstClass()),
            Sql.SQL_PRIMARY_KEY_NAME))
        .append("\n        ON UPDATE NO ACTION")
        .append("\n        ON DELETE NO ACTION")
        .append(String.format(
            ",\n    CONSTRAINT %s FOREIGN KEY(%s)",
            Sql.buildConstraintsNameForRelationalTableForeignKeyField(
                relation,
                relation.getSecondClass()),
            Sql.buildForeignKeyName(relation.getSecondClass().getName())))
        .append(String.format(
            "\n        REFERENCES %s(%s)",
            Sql.buildTableName(relation.getSecondClass()),
            Sql.SQL_PRIMARY_KEY_NAME))
        .append("\n        ON UPDATE NO ACTION")
        .append("\n        ON DELETE NO ACTION")
        .toString();
  }

  private String buildRelationalTableForeignKeyIndices(@NonNull DxdFieldRelation relation) {
    return new StringBuffer()
        .append(String.format(
            "\nCREATE INDEX %s ON %s(%s);",
            Sql.buildIndexNameForForeignKeyField(relation, relation.getFirstClass()),
            Sql.buildTableName(relation),
            Sql.buildForeignKeyName(relation.getFirstClass().getName())))
        .append(String.format(
            "\nCREATE INDEX %s ON %s(%s);",
            Sql.buildIndexNameForForeignKeyField(relation, relation.getSecondClass()),
            Sql.buildTableName(relation),
            Sql.buildForeignKeyName(relation.getSecondClass().getName())))
        .append("\n")
        .toString();
  }
}
