package com.nilsign.generators;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.diagrams.database.dot.DotGeneratorException;
import com.nilsign.helper.FileHelper;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Generator {

  @NonNull
  protected final DxdModel dxdModel;

  protected abstract String getOutputDirectory();
  protected abstract String getOutputFileName();

  public String getOutputFilePath() {
    String filePath = FileHelper.createDirectoriesIfNotExist(getOutputDirectory());
    return FileHelper.normalizePath(filePath) + getOutputFileName().trim();
  }

  public File createOutputFile() {
    String filePath = getOutputFilePath();
    FileHelper.deleteFileIfExists(filePath);
    try {
      return FileHelper.createFileIfNotExists(filePath);
    } catch (IOException e) {
      throw new DotGeneratorException(
          String.format(
              "Couldn't create new generation target file at '%s'",
              filePath),
          e);
    }
  }
}
