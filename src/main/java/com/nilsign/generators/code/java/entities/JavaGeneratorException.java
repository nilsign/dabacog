package com.nilsign.generators.code.java.entities;

import lombok.NonNull;

public final class JavaGeneratorException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Failed to generate java code.";

  public JavaGeneratorException(@NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }

  public JavaGeneratorException(@NonNull String error) {
    super(String.format("%s %s", ERROR_MESSAGE, error));
  }
}
