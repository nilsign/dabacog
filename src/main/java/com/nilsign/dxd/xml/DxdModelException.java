package com.nilsign.dxd.xml;

import lombok.NonNull;

public class DxdModelException extends Exception {

  private static final String ERROR_CLASS_MESSAGE = "Failed to build the DXD model.";

  public DxdModelException(@NonNull String message, @NonNull Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
