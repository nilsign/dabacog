package com.nilsign.generators.diagrams;

import lombok.NonNull;

public class GraphmlRendererException extends Exception {

  private static final String ERROR_CLASS_MESSAGE = "Failed to render graphml.";

  public GraphmlRendererException(@NonNull String message, @NonNull Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
