package com.nilsign;

import com.nilsign.cli.RootCommand;
import com.nilsign.logging.Logger;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import picocli.CommandLine;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Dabacog {

  public static final String DABACOG_VERSION = "0.1.0 - Alpha";

  public static final CommandLine CLI = new CommandLine(new RootCommand());

  public static void main(@NonNull String[] arguments) {
    Logger.init();
    try {
      CLI.parseArgs(arguments);
    } catch(Exception e) {
      Logger.error("Dabacog CLI parsing failed. " + e.getMessage() + ".");
      Logger.log("Example: dabacog --source ./app.dxd -target sql code --verbose-logging");
      Logger.log("Try 'dabacog --help' for more information.");
      System.exit(-1);
    }
    try {
      ((RootCommand) CLI.getCommand()).call();
    } catch (Exception e) {
      Logger.error(e);
      System.exit(1);
    }
    System.exit(0);
  }
 }

