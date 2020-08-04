package com.nilsign.logging;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.PrintStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Logger {

  public static final PrintStream LOG_STREAM = System.out;

  private static Logger instance;

  @NonNull
  private boolean isActive;

  @NonNull
  private LogLevel logLevel;

  public static void init(boolean isActive, LogLevel logLevel) {
    if (instance == null) {
      instance = new Logger(isActive, logLevel);
    }
    instance.isActive = isActive;
    instance.logLevel = logLevel;
  }

  public static void log() {
    if (instance != null && instance.isActive) {
      LOG_STREAM.println();
    }
  }

  public static void log(String text) {
    if (instance != null && instance.isActive) {
      LOG_STREAM.println(text);;
    }
  }

  public static void verbose(String text) {
    if (instance != null && instance.isActive && instance.logLevel == LogLevel.VERBOSE) {
      LOG_STREAM.println(text);;
    }
  }

  public static void printStackTrace(Exception e) {
    if (e != null
        && instance != null
        && instance.isActive
        && instance.logLevel == LogLevel.VERBOSE) {
      e.printStackTrace(LOG_STREAM);
    }
  }
}
