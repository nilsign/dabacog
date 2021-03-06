package com.nilsign.dxd;

import lombok.NonNull;

public final class DxdModelException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Failed to build the Dxd model.";

  public DxdModelException(@NonNull Exception e) {
    super(ERROR_MESSAGE, e);
  }
}
