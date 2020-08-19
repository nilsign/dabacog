package com.nilsign.dxd.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class DxdConfig {

  public static final String DEFAULT_DATABASE_DIAGRAM_OUTPUT_PATH = "./generated";
  public static final String DEFAULT_DATABASE_DIAGRAM_TITLE = "Dabacog - Database Scheme Diagram";
  public static final boolean DEFAULT_DATABASE_DIAGRAM_PRIMARY_KEY_FIELD_PORTS = false;
  public static final boolean DEFAULT_DATABASE_DIAGRAM_FOREIGN_KEY_FIELD_PORTS = true;

  public static final String DEFAULT_SQL_OUTPUT_PATH = "./generated";
  public static final boolean DEFAULT_SQL_GLOBAL_SEQUENCE = true;
  public static final boolean DEFAULT_SQL_DUMP_DATABASE = false;

  // Diagram
  @NonNull
  private String diagramDatabaseOutputPath;

  @NonNull
  private String diagramDatabaseTitle;

  @NonNull
  private boolean diagramDatabasePrimaryKeyFieldPorts;

  @NonNull
  private boolean diagramDatabaseForeignKeyFieldPorts;

  // SQL
  @NonNull
  private String sqlOutputPath;

  @NonNull
  private boolean sqlGlobalSequence;

  @NonNull
  private boolean sqlDumpDatabase;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, DxdConfig.class.getSimpleName()))
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
            "SqlDumpDatabase",
            sqlDumpDatabase))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
