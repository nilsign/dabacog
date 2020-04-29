package com.nilsign;

import com.nilsign.dxd.DxdModel;
import com.nilsign.dxd.DxdReader;
import com.nilsign.dxd.DxdReaderException;

import java.io.IOException;
import java.util.Arrays;

public class Dabacog {

  private static final String DABACOG_VERSION = "0.0.1";

  private static final String DXD_FILE_PATH = "./src/main/resources/dev/library.dxd";

  private static boolean flagDebug = false;
  private static boolean flagShowVersion = false;

  public static void main(String[] arguments) throws Exception {
    Dabacog.printDabacog();

    if (arguments != null && arguments[0].equals("-v")) {
      System.exit(0);;
    }

    System.out.println(String.format("Parsing DXD file: (%s)", DXD_FILE_PATH));
    System.out.println(Dabacog.readDxdModel().toString());
  }

  private static void extractFlagsFromArguments(String[] arguments) {
    extractFlagFromArguments(arguments, "-v", "--version", flagShowVersion);
    extractFlagFromArguments(arguments, "-d", "--debug", flagDebug);
  }

  private static void extractFlagFromArguments(
      String[] arguments, String shortArgument, String longArgument, boolean flagToSet) {
    flagToSet = Arrays.stream(arguments).anyMatch(argument
        -> argument.equalsIgnoreCase(shortArgument)
        || argument.equalsIgnoreCase(longArgument));
  }

  private static void printDabacog() throws IOException {
    System.out.println("    ____        __");
    System.out.println("   / __ \\____ _/ /_  ____ __________  ____ _");
    System.out.println("  / / / / __ `/ __ \\/ __ `/ ___/ __ \\/ __ `/");
    System.out.println(" / /_/ / /_/ / /_/ / /_/ / /__/ /_/ / /_/ /");
    System.out.println("/_____/\\__,_/_,___/\\__,_/\\___/\\____/\\__, /");
    System.out.println("                                   /____/");
    System.out.println(String.format("Version %s - development", DABACOG_VERSION));
    System.out.println();
  }

  private static DxdModel readDxdModel() {
    try {
      return DxdReader.run(Dabacog.DXD_FILE_PATH);
    } catch (DxdReaderException e) {
      if (!flagDebug) {
        e.printStackTrace();
      } else {
        System.out.println(e.getMessage());
      };
      System.exit(-1);
    }
    return null;
  }
}
