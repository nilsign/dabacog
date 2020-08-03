package com.nilsign.cli;

import com.nilsign.Dabacog;
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
import lombok.NonNull;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(
    description = "Example: dabacog --source ./app.dxd -target sql code --verbose-logging\n\n"
      + "Dabacog CLI arguments:\n",
    name="dabacog",
    mixinStandardHelpOptions = true,
    version = Dabacog.DABACOG_VERSION)
public class RootCommand implements Callable<Integer> {

  private static final String TARGET_VALUE_DIAGRAM_SHORT = "d";
  private static final String TARGET_VALUE_DIAGRAM = "diagram";
  private static final String TARGET_VALUE_SQL_SHORT = "s";
  private static final String TARGET_VALUE_SQL = "sql";
  private static final String TARGET_VALUE_CODE_SHORT = "c";
  private static final String TARGET_VALUE_CODE = "code";

  @Option(
      names = {"-s", "--source"},
      description = "Defines Dabacog description (Dxd) source file path.",
      required = true,
      arity = "1"
  )
  private File source;

  @Option(
      names = {"-t", "--targets"},
      description = "Defines the targets to generate. Use "
          + "'" + TARGET_VALUE_DIAGRAM_SHORT + "' or '"+ TARGET_VALUE_DIAGRAM + "', "
          + "'" + TARGET_VALUE_SQL_SHORT + "' or '" + TARGET_VALUE_SQL + "' and "
          + "'" + TARGET_VALUE_CODE_SHORT + "' or '" + TARGET_VALUE_CODE + "' "
          + "as values. The values can be combined space separated. If no target is defined all "
          + "targets are generated.",
      arity = "1..3"
  )
  private Set<String> targets;

  @Option(
      names = {"-n", "--no-logging"},
      description = "Disables logging to the console.",
      arity = "0"
  )
  private boolean isNoLogging;

  @Option(
      names = {"-v", "--verbose-logging"},
      description = "Enables verbose logging. Overrides the no-logging option.",
      arity = "0"
  )
  private boolean isVerboseLogging;

  @NonNull
  private XmlModel xmlModel;

  @NonNull
  private DxdModel dxdModel;

  @Override
  public Integer call() {
    printDabacogVersionInfo();
    if (Dabacog.CLI.isVersionHelpRequested()) {
      return 0;
    }
      if (Dabacog.CLI.isUsageHelpRequested()) {
      Dabacog.CLI.usage(System.out);
      return 0;
    }
    try {
      readXmlFile();
      buildDxdModel();
      if (isDiagramGenerationIncluded()) {
        generateDotDatabaseDiagram();
        renderDotDatabaseDiagram();
      }
      if (isSqlGenerationIncluded()) {
        generateSql();
      }
      if (isCodeGenerationIncluded()) {
        generateCode();
      }
      return 0;
    } catch (Exception e) {
      if (isVerboseLogging) {
        e.printStackTrace();
      } else {
        System.out.println(e.getMessage());
      };
      return 1;
    }
  }

  private void printDabacogVersionInfo() {
    System.out.println("    ____        __");
    System.out.println("   / __ \\____ _/ /_  ____ __________  ____ _");
    System.out.println("  / / / / __ `/ __ \\/ __ `/ ___/ __ \\/ __ `/");
    System.out.println(" / /_/ / /_/ / /_/ / /_/ / /__/ /_/ / /_/ /");
    System.out.println("/_____/\\__,_/_,___/\\__,_/\\___/\\____/\\__, /");
    System.out.println("                                   /____/");
    System.out.println(String.format("Version %s", Dabacog.DABACOG_VERSION));
    System.out.println();
  }

  private void readXmlFile() throws XmlReaderException {
    System.out.println(String.format("Parsing Dxd file '%s'...", source.getPath()));
    xmlModel = XmlReader.run(source.getPath());
    System.out.println(String.format("Parsing Dxd file -> [DONE]", source.getPath())) ;
    if (isVerboseLogging) {
      System.out.print(xmlModel.toString());
    }
  }

  private void buildDxdModel() throws DxdModelException {
    System.out.println(String.format("Preparing Dxd Model..."));
    dxdModel = XmlToDxdConverter.run(xmlModel);
    System.out.println(String.format("Preparing Dxd Model -> [DONE]"));
    if (isVerboseLogging) {
      System.out.print(dxdModel.toString());
    }
  }

  private boolean isDiagramGenerationIncluded() {
    return targets == null
        || targets.contains(TARGET_VALUE_DIAGRAM)
        || targets.contains(TARGET_VALUE_DIAGRAM_SHORT);
  }

  private void generateDotDatabaseDiagram() throws DotGeneratorException {
    System.out.println(String.format("Generating database diagram description..."));
    DotDatabaseDiagramGenerator.run(dxdModel);
    System.out.println(String.format("Generating database diagram description -> [DONE]"));
  }

  private void renderDotDatabaseDiagram() throws GraphvizDotRendererException {
    System.out.println(String.format("Rendering database diagram..."));
    GraphvizDotRenderer.run(dxdModel);
    System.out.println(String.format("Rendering database diagram -> [DONE]"));
  }

  private boolean isSqlGenerationIncluded() {
    return targets == null
        || targets.contains(TARGET_VALUE_SQL)
        || targets.contains(TARGET_VALUE_SQL_SHORT);
  }

  private void generateSql() {
    System.out.println(String.format("Generating SQL..."));
    System.out.println(String.format("WARNING: Not implemented yet."));
  }

  private boolean isCodeGenerationIncluded() {
    return targets == null
        || targets.contains(TARGET_VALUE_CODE)
        || targets.contains(TARGET_VALUE_CODE_SHORT);
  }

  private void generateCode() {
    System.out.println(String.format("Generating code..."));
    System.out.println(String.format("WARNING: Not implemented yet."));
  }
}
