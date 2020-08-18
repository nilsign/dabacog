package com.nilsign.generators.database.postgresql;

import lombok.NonNull;

public final class PostgreSqlGeneratorException extends RuntimeException {

  private static final String ERROR_MESSAGE
      = "Failed to generate the database sql schema script file.";

  public PostgreSqlGeneratorException(@NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }
}
