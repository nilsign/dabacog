package com.nilsign.generators.diagram;

public class GraphGeneratorException extends Exception {

  private static final String ERROR_CLASS_MESSAGE = "Failed to generate graph description file.";

  public GraphGeneratorException(String message, Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
