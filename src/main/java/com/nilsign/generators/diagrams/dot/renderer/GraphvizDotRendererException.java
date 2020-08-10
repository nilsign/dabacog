package com.nilsign.generators.diagrams.dot.renderer;

import lombok.NonNull;

public final class GraphvizDotRendererException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Failed to render Dot file.";

  public GraphvizDotRendererException(@NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }
}
