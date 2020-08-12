package com.nilsign.dxd.model;

import lombok.Data;
import lombok.NonNull;

@Data
public final class DxdConfig {

  private static final String DEFAULT_OUTPUT_PATH = "./generated";

  // Diagram
  @NonNull
  private String diagramDatabaseOutputPath = DEFAULT_OUTPUT_PATH;

  @NonNull
  private String diagramDatabaseTitle = "Dabacog - Database Scheme Diagram";

  @NonNull
  private boolean diagramDatabasePrimaryKeyFieldPorts = false;

  @NonNull
  private boolean diagramDatabaseForeignKeyFieldPorts = true;

  // SQL
  @NonNull
  private String sqlOutputPath = DEFAULT_OUTPUT_PATH;

  @NonNull
  private boolean sqlGlobalSequence = true;

  @NonNull
  private boolean sqlDeleteExistingSqlScripts = false;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, DxdConfig.class.getSimpleName()))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DefaultOutputPath",
            DEFAULT_OUTPUT_PATH))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DiagramDatabaseOutputPath",
            diagramDatabaseOutputPath))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DiagramDatabaseTitle",
            diagramDatabaseTitle))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DiagramDatabasePrimaryKeyFieldPorts",
            diagramDatabasePrimaryKeyFieldPorts))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DiagramDatabaseForeignKeyFieldPorts",
            diagramDatabaseForeignKeyFieldPorts))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "SqlOutputPath",
            sqlOutputPath))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "SqlGlobalSequence",
            sqlGlobalSequence))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "SqlDeleteExistingSqlScripts",
            sqlDeleteExistingSqlScripts))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
