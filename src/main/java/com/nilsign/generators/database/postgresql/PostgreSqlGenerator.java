package com.nilsign.generators.database.postgresql;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.GeneratedFilePaths;
import com.nilsign.generators.Generator;
import com.nilsign.generators.diagrams.dot.database.DotDatabaseDiagramGeneratorException;
import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;

public final class PostgreSqlGenerator extends Generator {

  public static final String OUTPUT_FILE_NAME = "InitializeDatabase.sql";

  private PostgreSqlGenerator(@NonNull DxdModel dxdModel) {
    super(dxdModel);
  }

  private static PostgreSqlGenerator of(@NonNull DxdModel dxdModel) {
    return new PostgreSqlGenerator(dxdModel);
  }

  public static void run(@NonNull DxdModel model) {
    try {
      PostgreSqlGenerator.of(model).generate();
    } catch (Exception e) {
      throw new DotDatabaseDiagramGeneratorException(e);
    }
  }

  @Override
  protected String getOutputDirectory() {
    return super.dxdModel.getConfig().getDiagramDatabaseOutputPath();
  }

  @Override
  protected String getOutputFileName() {
    return OUTPUT_FILE_NAME;
  }

  private void generate() {
    File outputFile = super.createOutputFile();
    try (FileWriter writer = new FileWriter(outputFile)) {

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
