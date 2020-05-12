package com.nilsign.dxd.xml;

public class DxdModelException extends Exception {

  private static final String ERROR_CLASS_MESSAGE = "Failed to build the DXD model.";

  public DxdModelException(String message, Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}
