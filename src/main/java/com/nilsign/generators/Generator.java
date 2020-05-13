package com.nilsign.generators;

import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.generators.diagrams.GraphmlGeneratorException;
import com.nilsign.helper.FileHelper;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;

public abstract class Generator {

  protected final DxdModel dxdModel;

  protected Generator(@NonNull DxdModel dxdModel) {
    this.dxdModel = dxdModel;
  }

  protected abstract String getOutputDirectory();
  protected abstract String getTargetFileName();

  public String getTargetFilePath() {
    String filePath = FileHelper.createDirectoriesIfNotExist(getOutputDirectory());
    return FileHelper.normalizePath(filePath) + getTargetFileName().trim();
  }

  public File createGenerationTargetFile() throws GraphmlGeneratorException {
    String filePath = getTargetFilePath();
    FileHelper.deleteFileIfExists(filePath);
    try {
      return FileHelper.createFileIfNotExists(filePath);
    } catch (IOException e) {
      throw new GraphmlGeneratorException(
          String.format(
              "Couldn't create new generation target file at '%s'",
              filePath),
          e);
    }
  }
}
