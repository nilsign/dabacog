package com.nilsign;

import com.nilsign.cli.RootCommand;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import picocli.CommandLine;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Dabacog {

  @NonNull Object command;
  public static final CommandLine CLI = new CommandLine(new RootCommand());

  @NonNull
  public static final String DABACOG_VERSION = "0.1.0 alpha";

  public static void main(@NonNull String[] arguments) {
    int exitCode = -1;
    try {
      CLI.parseArgs(arguments);
    } catch(Exception e) {
      System.out.println("ERROR: Dabacog CLI syntax. " + e.getMessage() + ".");
      System.out.println("Example: dabacog --source ./app.dxd -target sql code --verbose-logging");
      System.out.println("Try 'dabacog --help' for more information.");
      System.exit(exitCode);
    }
    try {
      exitCode = ((RootCommand) CLI.getCommand()).call();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    } finally {
      System.exit(exitCode);
    }
  }
 }

