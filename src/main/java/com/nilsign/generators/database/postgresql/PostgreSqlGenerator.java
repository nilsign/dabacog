package com.nilsign.generators.database.postgresql;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.GeneratedFilePaths;
import com.nilsign.generators.Generator;
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
      writer.write(new StringBuffer()
          .append(Generator.buildGeneratedByComment())
          .append(PostgreSqlSchemaBuilder.buildSettings(super.model))
          .append(PostgreSqlSchemaBuilder.buildDropSchema(super.model))
          .append(PostgreSqlSchemaBuilder.buildGlobalSequence(super.model))
          .append(PostgreSqlSchemaBuilder.buildTables(super.model))
          .append(PostgreSqlSchemaBuilder.buildForeignKeys(super.model))
          .append(PostgreSqlSchemaBuilder.buildIndices(super.model))
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
}
