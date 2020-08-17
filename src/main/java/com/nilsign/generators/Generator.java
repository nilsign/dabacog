package com.nilsign.generators;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.helper.FileHelper;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.io.File;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Generator {

  @NonNull
  protected final DxdModel model;

  protected abstract String getOutputDirectory();
  protected abstract String getOutputFileName();

  public String getOutputFilePath() {
    String filePath = FileHelper.createDirectoriesIfNotExist(getOutputDirectory());
    if (filePath.startsWith(".\\") || (filePath.startsWith("./"))){
      filePath = filePath.substring(2);
    }
    return FileHelper.normalizePath(filePath) + getOutputFileName().trim();
  }

  public File createOutputFile() {
    String filePath = getOutputFilePath();
    FileHelper.deleteFileIfExists(filePath);
    try {
      return FileHelper.createFileIfNotExists(filePath);
    } catch (Exception e) {
      throw new RuntimeException(
          String.format("Failed to create target file '%s'.", filePath),
          e);
    }
  }
}
