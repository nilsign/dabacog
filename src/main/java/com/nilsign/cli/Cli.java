package com.nilsign.cli;

import com.nilsign.logging.Logger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import picocli.CommandLine;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Cli {

  private static final CommandLine CLI = new CommandLine(new RootCommand());

  public static void parseArguments(@NonNull String[] arguments) {
    CLI.parseArgs(arguments);
  }

  public static void runCommand() {
    ((RootCommand) CLI.getCommand()).call();
  }

  public static void showHelp() {
    CLI.usage(Logger.LOG_STREAM);
  }

  public static boolean isHelpRequested() {
    return CLI.isUsageHelpRequested();
  }

  public static boolean isVersionRequested() {
    return CLI.isVersionHelpRequested();
  }
}
