package com.nilsign;

import com.nilsign.dxd.DxdReader;
import com.nilsign.dxd.DxdReaderException;
import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.dxd.xml.DxdModelException;
import com.nilsign.generators.diagrams.GraphmlDatabaseDiagramGenerator;
import com.nilsign.generators.diagrams.GraphmlGeneratorException;
import com.nilsign.generators.diagrams.GraphmlRenderer;
import com.nilsign.generators.diagrams.GraphmlRendererException;
import lombok.NonNull;

import java.util.Arrays;

public class Dabacog {

  private static final String DABACOG_VERSION = "0.0.1";
  private static final String DXD_FILE_PATH = "./src/main/resources/dev/library.dxd";

  private static boolean flagDebug = false;
  private static boolean flagShowVersion = false;

  private static DxdModel dxdModel;

  // TODO(nilsheumer): User picocli as framework to create the CLI.
  // https://github.com/remkop/picocli
  public static void main(String[] arguments) throws Exception {
    try {
      Dabacog.printDabacog();
      extractFlagsFromArguments(arguments);
      if (arguments != null && arguments.length > 0 && arguments[0].equals("-v")) {
        System.exit(0);;
      }
      Dabacog.readDxdModel();
      Dabacog.prepareDxdModel();
      Dabacog.generateGraphmlDatabaseDiagram();
      Dabacog.renderGraphmlDatabaseDiagram();
    } catch (Exception e) {
      if (flagDebug) {
        e.printStackTrace();
      } else {
        System.out.println(e.getMessage());
      };
      System.exit(-1);
    }
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

  private static void extractFlagsFromArguments(@NonNull String[] arguments) {
    flagShowVersion = extractFlagFromArguments(arguments, "-v", "--version");
    flagDebug = extractFlagFromArguments(arguments, "-d", "--debug");
  }

  private static boolean extractFlagFromArguments(
      @NonNull String[] arguments, @NonNull String shortArgument, @NonNull String longArgument) {
    return Arrays.stream(arguments).anyMatch(argument
        -> argument.equalsIgnoreCase(shortArgument) || argument.equalsIgnoreCase(longArgument));
  }

  private static void readDxdModel() throws DxdReaderException {
    System.out.println(String.format("Parsing DXD file '%s'...", DXD_FILE_PATH));
    dxdModel = DxdReader.run(Dabacog.DXD_FILE_PATH);
    if (flagDebug) {
      System.out.println(String.format("DXD MODEL\n%s", dxdModel.toString()));
    }
    System.out.println(String.format("Parsing DXD file -> [DONE]", DXD_FILE_PATH)) ;
  }

  private static void prepareDxdModel() throws DxdModelException {
    System.out.println(String.format("Preparing Dxd Model..."));
    dxdModel.getEntities().prepareModels();
    if (flagDebug) {
      printRelations(dxdModel);
    }
    System.out.println(String.format("Preparing Dxd Model -> [DONE]"));
  }

  private static void generateGraphmlDatabaseDiagram() throws GraphmlGeneratorException {
    System.out.println(String.format("Generating database diagram description..."));
    GraphmlDatabaseDiagramGenerator.run(dxdModel);
    System.out.println(String.format("Generating database diagram description -> [DONE]"));
  }

  public static void renderGraphmlDatabaseDiagram() throws GraphmlRendererException {
    System.out.println(String.format("Rendering database diagram..."));
    GraphmlRenderer.run(dxdModel);
    System.out.println(String.format("Rendering database diagram -> [DONE]"));
  }

  private static void printRelations(@NonNull DxdModel model) {
    System.out.println("DISTINCT MANY-TO-MANY-RELATIONS");
    model.getEntities().getManyToManyRelations().forEach(relation
        -> System.out.println(String.format("+ %s", relation.toString())));
    System.out.println("DISTINCT MANY-TO-ONE-RELATIONS");
    model.getEntities().getManyToOneRelations().forEach(relation
        -> System.out.println(String.format("+ %s", relation.toString())));
    System.out.println("DISTINCT ONE-TO-MANY-RELATIONS");
    model.getEntities().getOneToManyRelations().forEach(relation
        -> System.out.println(String.format("+ %s", relation.toString())));
    System.out.println("DISTINCT ONE-TO-ONE-RELATIONS");
    model.getEntities().getOneToOneRelations().forEach(relation
        -> System.out.println(String.format("+ %s", relation.toString())));
  }
}
