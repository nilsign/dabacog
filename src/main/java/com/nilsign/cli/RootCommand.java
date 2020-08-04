package com.nilsign.cli;

import com.nilsign.Dabacog;
import com.nilsign.dxd.DxdModelException;
import com.nilsign.dxd.XmlToDxdConverter;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.diagrams.database.GraphvizDotRenderer;
import com.nilsign.generators.diagrams.database.GraphvizDotRendererException;
import com.nilsign.generators.diagrams.database.dot.DotDatabaseDiagramGenerator;
import com.nilsign.generators.diagrams.database.dot.DotGeneratorException;
import com.nilsign.logging.LogLevel;
import com.nilsign.logging.Logger;
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
    updateLoggerConfiguration();
    printDabacogVersionInfo();
    if (Dabacog.CLI.isVersionHelpRequested()) {
      return 0;
    }
    if (Dabacog.CLI.isUsageHelpRequested()) {
      Dabacog.CLI.usage(Logger.LOG_STREAM);
      return 0;
    }
    try {
      readXmlFile();
      buildDxdModel();
      generateDotDatabaseDiagram();
      renderDotDatabaseDiagram();
      generateSql();
      generateCode();
      return 0;
    } catch (Exception e) {
        Logger.log(e.getMessage());
        Logger.printStackTrace(e);
      return 1;
    }
  }

  private void updateLoggerConfiguration() {
    Logger.setLogLevel(isVerboseLogging ? LogLevel.VERBOSE : LogLevel.DEFAULT);
    if (isNoLogging) {
      Logger.stop();
    }
  }

  private void printDabacogVersionInfo() {
    Logger.log("    ____        __");
    Logger.log("   / __ \\____ _/ /_  ____ __________  ____ _");
    Logger.log("  / / / / __ `/ __ \\/ __ `/ ___/ __ \\/ __ `/");
    Logger.log(" / /_/ / /_/ / /_/ / /_/ / /__/ /_/ / /_/ /");
    Logger.log("/_____/\\__,_/_,___/\\__,_/\\___/\\____/\\__, /");
    Logger.log("                                   /____/");
    Logger.log(String.format("Version %s", Dabacog.DABACOG_VERSION));
    Logger.log();
  }

  private void readXmlFile() throws XmlReaderException {
    Logger.log(String.format("Parsing Dxd file '%s'...", source.getPath()));
    xmlModel = XmlReader.run(source.getPath());
    Logger.log(String.format("Parsing Dxd file -> [DONE]", source.getPath()));
    Logger.verbose(xmlModel.toString());
  }

  private void buildDxdModel() throws DxdModelException {
    Logger.log(String.format("Preparing Dxd Model..."));
    dxdModel = XmlToDxdConverter.run(xmlModel);
    Logger.log(String.format("Preparing Dxd Model -> [DONE]"));
    Logger.verbose(dxdModel.toString());
  }

  private boolean hasDiagramTarget() {
    return targets == null
        || targets.contains(TARGET_VALUE_DIAGRAM)
        || targets.contains(TARGET_VALUE_DIAGRAM_SHORT);
  }

  private void generateDotDatabaseDiagram() throws DotGeneratorException {
    if (hasDiagramTarget()) {
      Logger.log(String.format("Generating database diagram description..."));
      DotDatabaseDiagramGenerator.run(dxdModel);
      Logger.log(String.format("Generating database diagram description -> [DONE]"));
    }
  }

  private void renderDotDatabaseDiagram() throws GraphvizDotRendererException {
    if (hasDiagramTarget()) {
      Logger.log(String.format("Rendering database diagram..."));
      GraphvizDotRenderer.run(dxdModel);
      Logger.log(String.format("Rendering database diagram -> [DONE]"));
    }
  }

  private boolean hasSqlTarget() {
    return targets == null
        || targets.contains(TARGET_VALUE_SQL)
        || targets.contains(TARGET_VALUE_SQL_SHORT);
  }

  private void generateSql() {
    if (hasSqlTarget()) {
      Logger.log(String.format("Generating SQL..."));
      Logger.log(String.format("WARNING: Not implemented yet."));
    }
  }

  private boolean hasCodeTarget() {
    return targets == null
        || targets.contains(TARGET_VALUE_CODE)
        || targets.contains(TARGET_VALUE_CODE_SHORT);
  }

  private void generateCode() {
    if (hasCodeTarget()) {
      Logger.log(String.format("Generating code..."));
      Logger.log(String.format("WARNING: Not implemented yet."));
     }
  }
}
