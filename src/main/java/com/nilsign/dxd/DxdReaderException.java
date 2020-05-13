package com.nilsign.dxd;

import lombok.NonNull;

public class DxdReaderException extends Exception {

  private static final String ERROR_CLASS_MESSAGE = "Failed to read the DXD.";

  public DxdReaderException(@NonNull String message, @NonNull Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
