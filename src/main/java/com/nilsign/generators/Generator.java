package com.nilsign.generators;

import com.nilsign.dxd.elements.DxdModel;
import com.nilsign.generators.graphs.GraphGeneratorException;
import com.nilsign.helper.FileHelper;

import java.io.File;
import java.io.IOException;

public abstract class Generator {

  protected final DxdModel dxdModel;

  protected Generator(DxdModel dxdModel) {
    this.dxdModel = dxdModel;
  }

  protected abstract String getOutputDirectory();
  protected abstract String getTargetFileName();
  protected abstract void generate() throws GraphGeneratorException;

  public File createTargetFile() throws GraphGeneratorException {
    String filePath = FileHelper.createDirectoriesIfNotExist(getOutputDirectory());
    filePath = FileHelper.normalizePath(filePath) + getTargetFileName().trim();
    FileHelper.deleteFileIfExists(filePath);
    try {
      return FileHelper.createFileIfNotExists(filePath);
    } catch (IOException e) {
      throw new GraphGeneratorException(
          String.format(
              "Couldn't create graph description file at '%s'",
              filePath),
          e);
    }
  }
}
