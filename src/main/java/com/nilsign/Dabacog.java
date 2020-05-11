package com.nilsign;

import com.nilsign.dxd.DxdReader;
import com.nilsign.dxd.DxdReaderException;
import com.nilsign.dxd.elements.DxdModel;
import com.nilsign.generators.graphs.DatabaseGraphGenerator;

import java.util.Arrays;

public class Dabacog {

  private static final String DABACOG_VERSION = "0.0.1";
  private static final String DXD_FILE_PATH = "./src/main/resources/dev/library.dxd";

  private static boolean flagDebug = false;
  private static boolean flagShowVersion = false;

  // TODO(nilsheumer): User picocli as framework to create the CLI.
  // https://github.com/remkop/picocli
  public static void main(String[] arguments) throws Exception {
    Dabacog.printDabacog();

    extractFlagsFromArguments(arguments);
    if (arguments != null && arguments.length > 0 && arguments[0].equals("-v")) {
      System.exit(0);;
    }

    System.out.print(String.format("Parsing DXD file: '%s'", DXD_FILE_PATH));
    DxdModel dxdModel = Dabacog.readDxdModel();
    System.out.println(" -> [DONE]");
    if (flagDebug) {
      System.out.println(String.format("\nDXD MODEL\n%s\n", dxdModel.toString()));
    }

    System.out.print(String.format("Preparing Dxd Model"));
    dxdModel.getEntities().prepareModels();
    System.out.println(" -> [DONE]");
    if (flagDebug) {
     printDistinctRelations(dxdModel);
     printAllRelations(dxdModel);
     System.out.println();
    }

    System.out.print(String.format("Generating database diagram"));
    DatabaseGraphGenerator.run(dxdModel);
    System.out.println(" -> [DONE]");
  }

  private static void printDabacog() {
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

  private static void extractFlagsFromArguments(String[] arguments) {
    flagShowVersion = extractFlagFromArguments(arguments, "-v", "--version");
    flagDebug = extractFlagFromArguments(arguments, "-d", "--debug");
  }

  private static boolean extractFlagFromArguments(
      String[] arguments, String shortArgument, String longArgument) {
    return Arrays.stream(arguments).anyMatch(argument
        -> argument.equalsIgnoreCase(shortArgument)
        || argument.equalsIgnoreCase(longArgument));
  }

  private static void printDistinctRelations(DxdModel model) {
    System.out.println("\nDISTINCT MANY-TO-MANY-RELATIONS");
    model.getEntities().getDistinctManyToManyClassRelationsList().forEach(relation
        -> System.out.println(String.format(
            "+ %s -> %s",
            relation.getFirst().getName(),
            relation.getSecond().getName())));
    System.out.println("\nDISTINCT MANY-TO-ONE-RELATIONS");
    model.getEntities().getDistinctManyToOneClassRelationsList().forEach(relation
        -> System.out.println(String.format(
          "+ %s -> %s",
          relation.getFirst().getName(),
          relation.getSecond().getName())));
    System.out.println("\nDISTINCT ONE-TO-MANY-RELATIONS");
    model.getEntities().getDistinctOneToManyClassRelationsList().forEach(relation
        -> System.out.println(String.format(
          "+ %s -> %s",
          relation.getFirst().getName(),
          relation.getSecond().getName())));
    System.out.println("\nDISTINCT ONE-TO-ONE-RELATIONS");
    model.getEntities().getDistinctOneToOneClassRelationsList().forEach(relation
        -> System.out.println(String.format(
          "+ %s -> %s",
          relation.getFirst().getName(),
          relation.getSecond().getName())));
  }

  private static void printAllRelations(DxdModel model) {
    System.out.println("\nALL MANY-TO-MANY-RELATIONS");
    model.getEntities().getManyToManyClassRelationsMap()
        .forEach((dxdClass, referredDxdClasses)
            -> referredDxdClasses.forEach(referredDxdClass
                -> System.out.println(String.format(
                    "+ %s -> %s",
                    dxdClass.getName(),
                    referredDxdClass.getName()))));
    System.out.println("\nALL MANY-TO-ONE-RELATIONS");
    model.getEntities().getManyToOneClassRelationsMap()
        .forEach((dxdClass, referredDxdClasses)
            -> referredDxdClasses.forEach(referredDxdClass
                -> System.out.println(String.format(
                    "+ %s -> %s",
                    dxdClass.getName(),
                    referredDxdClass.getName()))));
    System.out.println("\nALL ONE-TO-MANY-RELATIONS");
    model.getEntities().getOneToManyClassRelationsMap()
        .forEach((dxdClass, referredDxdClasses)
            -> referredDxdClasses.forEach(referredDxdClass
                -> System.out.println(String.format(
                    "+ %s -> %s",
                    dxdClass.getName(),
                    referredDxdClass.getName()))));
    System.out.println("\nALL ONE-TO-ONE-RELATIONS");
    model.getEntities().getOneToOneClassRelationsMap()
        .forEach((dxdClass, referredDxdClasses)
            -> referredDxdClasses.forEach(referredDxdClass
               -> System.out.println(String.format(
                    "+ %s -> %s",
                    dxdClass.getName(),
                    referredDxdClass.getName()))));
  }
}
