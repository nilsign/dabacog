package com.nilsign.dxd.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public final class DxdConfig {

  @NonNull
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
        .toString();
  }

  public String toString() {
    return toString("");
  }
}
