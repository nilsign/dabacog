package com.nilsign.generators.diagrams;

public class GraphmlGeneratorException extends Exception {

  private static final String ERROR_CLASS_MESSAGE = "Failed to generate graph description file.";

  public GraphmlGeneratorException(String message, Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}