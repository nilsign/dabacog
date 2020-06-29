package com.nilsign.generators.diagrams.database;

import lombok.NonNull;

public class GraphvizDotRendererException extends RuntimeException {

  private static final String ERROR_CLASS_MESSAGE = "Failed to render graphml.";

  public GraphvizDotRendererException(@NonNull String message, @NonNull Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
