package com.nilsign.logging;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.PrintStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Logger {

  public static final PrintStream LOG_STREAM = System.out;

  @NonNull
  private static Logger instance;

  @NonNull
  private boolean isActive = true;

  @NonNull
  private LogLevel logLevel = LogLevel.DEFAULT;

  public static void init() {
    if (instance == null) {
      instance = new Logger();
    }
  }

  public static void stop() {
    instance.isActive = false;
  }

  public static void setLogLevel(@NonNull LogLevel logLevel) {
    instance.logLevel = logLevel;
  }

  public static void log() {
    if (instance != null && instance.isActive) {
      LOG_STREAM.println();
    }
  }

  public static void log(@NonNull String text) {
    if (instance != null && instance.isActive) {
      LOG_STREAM.println(text);;
    }
  }

  public static void print(@NonNull String text) {
    if (instance != null && instance.isActive) {
      LOG_STREAM.print(text);;
    }
  }

  public static void verbose(@NonNull String text) {
    if (instance != null && instance.isActive && instance.logLevel == LogLevel.VERBOSE) {
      LOG_STREAM.println(text);;
    }
  }

  public static void printStackTrace(@NonNull Exception e) {
    if (e != null
        && instance != null
        && instance.isActive
        && instance.logLevel == LogLevel.VERBOSE) {
      e.printStackTrace(LOG_STREAM);
    }
  }
}
