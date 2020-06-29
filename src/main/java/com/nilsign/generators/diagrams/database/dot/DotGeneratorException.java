package com.nilsign.generators.diagrams.database.dot;

import lombok.NonNull;

public class DotGeneratorException extends RuntimeException {

  private static final String ERROR_CLASS_MESSAGE = "Failed to generate graph description file.";

  public DotGeneratorException(@NonNull String message, @NonNull Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
