package com.nilsign.reader.xml;

import lombok.NonNull;

public final class XmlReaderException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Failed to read the source Dxd file.";

  public XmlReaderException(@NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }
}
