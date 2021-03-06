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

public final class JavaGenerator extends Generator {

  private String outputFileName;

  private JavaGenerator(@NonNull DxdModel model) {
    super(model);
  }

  private static JavaGenerator of(@NonNull DxdModel model) {
    return new JavaGenerator(model);
  }

  public static void run(@NonNull DxdModel model) {
    try {
      JavaGenerator.of(model).generate();
    } catch (Exception e) {
      throw new JavaGeneratorException(e);
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
            .append(JavaEntityClassBuilder.buildEntityClass(super.model, aClass))
            .toString());
      } catch (Exception e) {
        throw new RuntimeException(
            String.format("Failed to generate Java target file '%s'.", outputFile),
            e);
      } finally {
        codeFiles.add(outputFile.getAbsolutePath());
        GeneratedFilePaths.setCodeFiles(codeFiles);
      }
    });
    GeneratedFilePaths.setCodeFiles(codeFiles);
  }
}
