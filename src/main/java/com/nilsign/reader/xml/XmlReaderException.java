package com.nilsign.reader.xml;

import lombok.NonNull;

public final class XmlReaderException extends RuntimeException {

  private static final String ERROR_CLASS_MESSAGE = "Failed to read the input Xml file.";

  public XmlReaderException(@NonNull String message, @NonNull Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
