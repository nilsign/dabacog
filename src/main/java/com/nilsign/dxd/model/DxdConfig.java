package com.nilsign.dxd.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class DxdConfig {

  private static final String DEFAULT_OUTPUT_PATH = "./generated";

  // Diagram
  private String diagramDatabaseOutputPath = DEFAULT_OUTPUT_PATH;

  @NonNull
  private String diagramDatabaseTitle = "Dabacog - Database Scheme Diagram";

  @NonNull
  private boolean diagramDatabasePrimaryKeyFieldPorts = false;

  @NonNull
  private boolean diagramDatabaseForeignKeyFieldPorts = true;
}
