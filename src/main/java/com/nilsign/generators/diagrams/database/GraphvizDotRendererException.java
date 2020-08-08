package com.nilsign.generators.diagrams.database;

import lombok.NonNull;

public final class GraphvizDotRendererException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Failed to render graphml.";

  public GraphvizDotRendererException(@NonNull String message, @NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }
}
