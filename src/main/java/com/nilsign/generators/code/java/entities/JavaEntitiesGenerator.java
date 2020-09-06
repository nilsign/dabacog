package com.nilsign.generators.code.java.entities;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.GeneratedFilePaths;
import com.nilsign.generators.Generator;
import com.nilsign.generators.code.java.Java;
import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public final class JavaEntitiesGenerator extends Generator {

  private String outputFileName;

  private JavaEntitiesGenerator(@NonNull DxdModel model) {
    super(model);
  }

  private static JavaEntitiesGenerator of(@NonNull DxdModel model) {
    return new JavaEntitiesGenerator(model);
  }

  public static void run(@NonNull DxdModel model) {
    try {
      JavaEntitiesGenerator.of(model).generate();
    } catch (Exception e) {
      throw new JavaEntitiesGeneratorException(e);
    }
  }

  @Override
  protected String getOutputDirectory() {
    return super.model.getConfig().getCodeOutputPath();
  }

  @Override
  protected String getOutputFileName() {
    return outputFileName;
  }

  private void generate() {
    List<String> codeFiles = new ArrayList<>();
    super.model.getClasses().forEach(aClass -> {
      outputFileName = Java.buildJavaFileName(aClass.getName());
      File outputFile = super.createOutputFile();
      try (FileWriter writer = new FileWriter(outputFile)) {
        writer.write(new StringBuffer()
            .append(Java.buildGeneratedByComment())
            .toString());
      } catch (Exception e) {
        throw new RuntimeException(
            String.format(
                "Failed to write into target file '%s'.",
                outputFile),
            e);
      } finally {
        codeFiles.add(outputFile.getAbsolutePath());
        GeneratedFilePaths.setCodeFiles(codeFiles);
      }
    });
    GeneratedFilePaths.setCodeFiles(codeFiles);
  }
}
