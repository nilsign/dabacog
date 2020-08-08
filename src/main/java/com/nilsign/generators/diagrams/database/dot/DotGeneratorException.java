package com.nilsign.generators.diagrams.database.dot;

import lombok.NonNull;

public final class DotGeneratorException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Failed to generate graph description file.";

  public DotGeneratorException(@NonNull String message, @NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }
}
