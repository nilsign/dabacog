package com.nilsign.generators.diagrams;

public class GraphmlRendererException extends Exception {

  private static final String ERROR_CLASS_MESSAGE = "Failed to render graphml.";

  public GraphmlRendererException(String message, Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
