package com.nilsign.generators.diagrams.dot.database;

import lombok.NonNull;

public final class DotDatabaseDiagramGeneratorException extends RuntimeException {

  private static final String ERROR_MESSAGE
      = "Failed to generate the database diagram description Dot file.";

  public DotDatabaseDiagramGeneratorException(@NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }
}
