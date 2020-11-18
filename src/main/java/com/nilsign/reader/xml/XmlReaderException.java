package com.nilsign.reader.xml;

import lombok.NonNull;

public final class XmlReaderException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Failed to read '%s'.";

  public XmlReaderException(@NonNull Exception e, @NonNull String filePath) {
    super(String.format(ERROR_MESSAGE, filePath), e);
  }
}
