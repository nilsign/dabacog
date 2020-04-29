package com.nilsign.dxd;

public class DxdReaderException extends Exception {

  private static final String ERROR_CLASS_MESSAGE = "Read DXD file failed.";

  public DxdReaderException(String message, Exception e) {
    super(String.format("%s %s", ERROR_CLASS_MESSAGE, message), e);
  }
}