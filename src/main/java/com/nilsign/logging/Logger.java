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

  public static void log(@NonNull String text) {
    if (isDefaultLogLevel()) {
      LOG_STREAM.println(text);
    }
  }

  public static void error(@NonNull String text) {
    if (isDefaultLogLevel()) {
      LOG_STREAM.println("\nError: " + text);;
    }
  }

  public static void error(@NonNull Exception e) {
    if (isVerboseLogLevel()) {
      LOG_STREAM.print("\nStacktrace: ");
      e.printStackTrace(LOG_STREAM);
      return;
    }
    LOG_STREAM.println("\nError: " + e.getMessage());
    logCauses(e.getCause());
  }

  public static void verbose(@NonNull String text) {
    if (isVerboseLogLevel()) {
      LOG_STREAM.println(text);;
    }
  }

  public static void out(@NonNull String text) {
    if (isDefaultLogLevel()) {
      LOG_STREAM.print(text);;
    }
  }

  private static boolean isDefaultLogLevel() {
    return instance != null && instance.isActive;
  }

  private static boolean isVerboseLogLevel() {
    return instance != null
        && instance.isActive
        && instance.logLevel == LogLevel.VERBOSE;
  }

  private static void logCauses(Throwable throwable) {
    if (throwable != null) {
      if (throwable.getMessage() != null) {
        LOG_STREAM.println("Cause: " + throwable.getMessage());
      }
      logCauses(throwable.getCause());
    }
  }
}
