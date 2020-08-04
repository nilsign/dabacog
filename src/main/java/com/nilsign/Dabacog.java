package com.nilsign;

import com.nilsign.cli.RootCommand;
import com.nilsign.logging.LogLevel;
import com.nilsign.logging.Logger;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import picocli.CommandLine;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Dabacog {

  public static final String DABACOG_VERSION = "0.1.0 alpha";

  public static final CommandLine CLI = new CommandLine(new RootCommand());

  public static void main(@NonNull String[] arguments) {
    int exitCode = -1;
    Logger.init(true, LogLevel.DEFAULT);
    try {
      CLI.parseArgs(arguments);
    } catch(Exception e) {
      Logger.log("ERROR: Dabacog CLI parsing failed. " + e.getMessage() + ".");
      Logger.log("Example: dabacog --source ./app.dxd -target sql code --verbose-logging");
      Logger.log("Try 'dabacog --help' for more information.");
      System.exit(exitCode);
    }
    try {
      exitCode = ((RootCommand) CLI.getCommand()).call();
    } catch (Exception e) {
      Logger.log(e.getMessage());
    } finally {
      System.exit(exitCode);
    }
  }
 }
 
