package com.nilsign.generators;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeneratedFilePaths {

  private static GeneratedFilePaths instance;

  private String databaseDiagramDotFile;
  private String databaseDiagramFile;
  private String sqlScriptFile;

  public static GeneratedFilePaths of() {
    if (instance == null) {
      instance = new GeneratedFilePaths();
    }
    return instance;
  }

  public static void setDatabaseDiagramDotFile(@NonNull String filePath) {
    GeneratedFilePaths.of().databaseDiagramDotFile = filePath;
  }

  public static String getDatabaseDiagramDotFile() {
    return GeneratedFilePaths.of().databaseDiagramDotFile;
  }

  public static void setDatabaseDiagramFile(@NonNull String filePath) {
    GeneratedFilePaths.of().databaseDiagramFile = filePath;
  }

  public static String getDatabaseDiagramFile() {
    return GeneratedFilePaths.of().databaseDiagramFile;
  }

  public static void setSqlScriptFile(@NonNull String filePath) {
    GeneratedFilePaths.of().sqlScriptFile = filePath;
  }

  public static String getSqlScriptFile() {
    return GeneratedFilePaths.of().sqlScriptFile;
  }
}
