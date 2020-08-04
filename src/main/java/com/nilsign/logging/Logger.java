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

  public static void setLogLevel(LogLevel logLevel) {
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
