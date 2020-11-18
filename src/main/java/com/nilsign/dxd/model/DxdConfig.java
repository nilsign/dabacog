package com.nilsign.dxd.model;

import com.google.common.collect.ImmutableList;
import com.nilsign.dxd.types.CodeType;
import com.nilsign.dxd.types.DatabaseType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class DxdConfig {

  // Diagram default values
  public static final String DEFAULT_DATABASE_DIAGRAM_OUTPUT_PATH = "./generated";
  public static final String DEFAULT_DATABASE_DIAGRAM_TITLE = "Dabacog - Database Scheme Diagram";
  public static final boolean DEFAULT_DATABASE_DIAGRAM_PRIMARY_KEY_FIELD_PORTS = false;
  public static final boolean DEFAULT_DATABASE_DIAGRAM_FOREIGN_KEY_FIELD_PORTS = true;

  // Sql default values
  public static final DatabaseType DEFAULT_SQL_DATABASE_TYPE = DatabaseType.POSTGRESQL;
  public static final String DEFAULT_SQL_OUTPUT_PATH = "./generated";
  public static final boolean DEFAULT_SQL_GLOBAL_SEQUENCE = true;
  public static final boolean DEFAULT_SQL_DUMP_DATABASE = false;

  // Code default values
  public static final CodeType DEFAULT_CODE_DATABASE_TYPE = CodeType.JAVA;
  public static final String DEFAULT_CODE_OUTPUT_PATH = "./generated";

  @NonNull
  private String diagramDatabaseOutputPath;

  @NonNull
  private String diagramDatabaseTitle;

  @NonNull
  private boolean diagramDatabasePrimaryKeyFieldPorts;

  @NonNull
  private boolean diagramDatabaseForeignKeyFieldPorts;

  // Sql
  @NonNull
  private DatabaseType sqlDatabaseType;

  @NonNull
  private String sqlOutputPath;

  @NonNull
  private boolean sqlGlobalSequence;

  @NonNull
  private boolean sqlDropSchema;

  @NonNull
  private ImmutableList<DxdSqlConnection> sqlConnections;

  // Code
  @NonNull
  private CodeType codeType;

  @NonNull
  private String codeOutputPath;

  @NonNull
  private String codePackageName;

  @NonNull
  private String codePasswordsFile;

  @NonNull
  private String codePasswordsOutputPath;

  public String toString(@NonNull String indentation) {
    StringBuffer output = new StringBuffer()
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
            "SqlDropSchema",
            sqlDropSchema));
        sqlConnections.forEach(connection
            -> output.append(connection.toString("\t\t\t")));
        output.append(String.format("%s\t%s: %s\n",
            indentation,
            "CodeType",
            codeType))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "CodeOutputPath",
            codeOutputPath))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "CodePackageName",
            codePackageName))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "CodePasswordsFile",
            codePasswordsFile))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "codePasswordsOutputPath",
            codePasswordsOutputPath));
      return output.toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
