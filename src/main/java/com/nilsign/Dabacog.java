package com.nilsign;

import com.nilsign.dxd.DxdModelException;
import com.nilsign.dxd.XmlToDxdConverter;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.diagrams.database.GraphvizDotRenderer;
import com.nilsign.generators.diagrams.database.GraphvizDotRendererException;
import com.nilsign.generators.diagrams.database.dot.DotDatabaseDiagramGenerator;
import com.nilsign.generators.diagrams.database.dot.DotGeneratorException;
import com.nilsign.reader.xml.XmlReader;
import com.nilsign.reader.xml.XmlReaderException;
import com.nilsign.reader.xml.model.XmlModel;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Dabacog {

  @NonNull
  private static final String DABACOG_VERSION = "0.0.1";

  @NonNull
  private static final String DXD_FILE_PATH = "./src/main/resources/dev/library.dxd";

  @NonNull
  private static boolean flagDebug = false;

  @NonNull
  private static boolean flagShowVersion = false;

  @NonNull
  private static XmlModel xmlModel;

  @NonNull
  private static DxdModel dxdModel;

  // TODO(nilsheumer): User picocli as framework to create the CLI.
  // https://github.com/remkop/picocli
  public static void main(@NonNull String[] arguments) {
    try {
      extractFlagsFromArguments(arguments);
      if (arguments != null && arguments.length > 0 && arguments[0].equals("-v")) {
        System.exit(0);;
      }
      Dabacog.printDabacog();
      Dabacog.readXmlFile();
      Dabacog.buildDxdModel();
      Dabacog.generateDotDatabaseDiagram();
      Dabacog.renderDotDatabaseDiagram();
    } catch (Exception e) {
      if (flagDebug) {
        e.printStackTrace();
      } else {
        System.out.println(e.getMessage());
      };
      System.exit(-1);
    }
  }

  private static void extractFlagsFromArguments(@NonNull String[] arguments) {
    flagShowVersion = extractFlagFromArguments(arguments, "-v", "--version");
    flagDebug = extractFlagFromArguments(arguments, "-d", "--debug");
  }

  private static boolean extractFlagFromArguments(
      @NonNull String[] arguments,
      @NonNull String shortArgument,
      @NonNull String longArgument) {
    return Arrays.stream(arguments).anyMatch(argument
        -> argument.equalsIgnoreCase(shortArgument) || argument.equalsIgnoreCase(longArgument));
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

  private static void readXmlFile() throws XmlReaderException {
    System.out.println(String.format("Parsing Dxd file '%s'...", DXD_FILE_PATH));
    xmlModel = XmlReader.run(Dabacog.DXD_FILE_PATH);
    System.out.println(String.format("Parsing Dxd file -> [DONE]", DXD_FILE_PATH)) ;
    if (flagDebug) {
      System.out.print(xmlModel.toString());
    }
  }

  private static void buildDxdModel() throws DxdModelException {
    System.out.println(String.format("Preparing Dxd Model..."));
    dxdModel = XmlToDxdConverter.of(xmlModel).convert();
    System.out.println(String.format("Preparing Dxd Model -> [DONE]"));
    if (flagDebug) {
      System.out.print(dxdModel.toString());
    }
  }

  private static void generateDotDatabaseDiagram() throws DotGeneratorException {
    System.out.println(String.format("Generating database diagram description..."));
    DotDatabaseDiagramGenerator.run(dxdModel);
    System.out.println(String.format("Generating database diagram description -> [DONE]"));
  }

  public static void renderDotDatabaseDiagram() throws GraphvizDotRendererException {
    System.out.println(String.format("Rendering database diagram..."));
    GraphvizDotRenderer.run(dxdModel);
    System.out.println(String.format("Rendering database diagram -> [DONE]"));
  }
}
