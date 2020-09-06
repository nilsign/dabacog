package com.nilsign.generators.code.java.entities;

import lombok.NonNull;

public final class JavaEntitiesGeneratorException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Failed to generate java entities.";

  public JavaEntitiesGeneratorException(@NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }
}
