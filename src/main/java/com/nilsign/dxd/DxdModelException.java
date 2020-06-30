package com.nilsign.dxd;

import lombok.NonNull;

public class DxdModelException extends RuntimeException {

  private static final String ERROR_CLASS_MESSAGE = "Failed to build the Dxd model.";

  public DxdModelException(@NonNull String message, @NonNull Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }

  public DxdModelException(@NonNull String message) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message));
  }
}
