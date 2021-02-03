package com.nilsign.cli;

import com.nilsign.Dabacog;
import com.nilsign.dxd.XmlToDxdConverter;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.GeneratedFilePaths;
import com.nilsign.generators.code.java.entities.JavaGenerator;
import com.nilsign.generators.database.postgresql.PostgreSqlGenerator;
import com.nilsign.generators.diagrams.dot.database.DotDatabaseDiagramGenerator;
import com.nilsign.generators.diagrams.dot.renderer.GraphvizDotRenderer;
import com.nilsign.logging.LogLevel;
import com.nilsign.logging.Logger;
import com.nilsign.reader.xml.XmlReader;
import com.nilsign.reader.xml.model.XmlDxdModel;
import lombok.NonNull;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(
    description = "Example: dabacog --source ./app.dxd --targets sql code --verbose-logging\n\n"
      + "Dabacog CLI arguments:\n",
    name = "dabacog",
    mixinStandardHelpOptions = true,
    version = Dabacog.DABACOG_VERSION)
public final class RootCommand implements Callable<Integer> {

  private static final String TARGET_VALUE_DIAGRAM_SHORT = "d";
  private static final String TARGET_VALUE_DIAGRAM = "diagram";
  private static final String TARGET_VALUE_SQL_SHORT = "s";
  private static final String TARGET_VALUE_SQL = "sql";
  private static final String TARGET_VALUE_CODE_SHORT = "c";
  private static final String TARGET_VALUE_CODE = "code";


  @Option(
      names = {"-s", "--source"},
      description = "Defines the description (Dxd) source file path.",
      required = true,
      arity = "1"
  )
  @SuppressWarnings("unused")
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
  @SuppressWarnings("unused")
  private Set<String> targets;

  @Option(
      names = {"-n", "--no-logging"},
      description = "Disables logging to the console.",
      arity = "0"
  )
  @SuppressWarnings("unused")
  private boolean isNoLogging;

  @Option(
      names = {"-v", "--verbose-logging"},
      description = "Enables verbose logging. Overrides the no-logging option.",
      arity = "0"
  )
  @SuppressWarnings("unused")
  private boolean isVerboseLogging;

  @NonNull
  private XmlDxdModel xmlDxdModel;

  @NonNull
  private DxdModel dxdModel;

  @Override
  public Integer call() {
    updateLoggerConfiguration();
    printDabacogVersionInfo();
    if (Cli.isHelpRequested()) {
      Cli.showHelp();
      return 0;
    }
    if (Cli.isVersionRequested()) {
      return 0;
    }
    readXmlDxdFile();
    buildDxdModel();
    generateDotDatabaseDiagram();
    renderDotDatabaseDiagram();
    generateSql();
    generateCode();
    return 0;
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
    Logger.log("");
  }

  private void readXmlDxdFile()  {
    Logger.out(String.format("Parsing Dxd file '%s' ... ", source.getPath()));
    xmlDxdModel = XmlReader.run(
        source.getPath(),
        XmlDxdModel.class);
    Logger.log("[DONE]");
    Logger.verbose(xmlDxdModel.toString());
  }

  private void buildDxdModel() {
    Logger.out("Preparing Dxd Model ... ");
    dxdModel = XmlToDxdConverter.run(xmlDxdModel);
    Logger.log("[DONE]");
    Logger.verbose(dxdModel.toString());
  }

  private boolean hasDiagramTarget() {
    return targets == null
        || targets.contains(TARGET_VALUE_DIAGRAM)
        || targets.contains(TARGET_VALUE_DIAGRAM_SHORT);
  }

  private void generateDotDatabaseDiagram() {
    if (hasDiagramTarget()) {
      Logger.out("Generating database diagram description ... ");
      DotDatabaseDiagramGenerator.run(dxdModel);
      Logger.log("[DONE]");
      Logger.verbose(String.format("\t%s\n", GeneratedFilePaths.getDatabaseDiagramDotFile()));
    }
  }

  private void renderDotDatabaseDiagram() {
    if (hasDiagramTarget()) {
      Logger.out("Rendering database diagram ... ");
      GraphvizDotRenderer.run(dxdModel);
      Logger.log("[DONE]");
      Logger.verbose(String.format("\t%s\n", GeneratedFilePaths.getDatabaseDiagramFile()));
    }
  }

  private boolean hasSqlTarget() {
    return targets == null
        || targets.contains(TARGET_VALUE_SQL)
        || targets.contains(TARGET_VALUE_SQL_SHORT);
  }

  private void generateSql() {
    if (hasSqlTarget()) {
      Logger.out("Generating Sql ... ");
      PostgreSqlGenerator.run(dxdModel);
      Logger.log("[DONE]");
      Logger.verbose(String.format("\t%s\n", GeneratedFilePaths.getSqlScriptFile()));
    }
  }

  private boolean hasCodeTarget() {
    return targets == null
        || targets.contains(TARGET_VALUE_CODE)
        || targets.contains(TARGET_VALUE_CODE_SHORT);
  }

  private void generateCode() {
    if (hasCodeTarget()) {
      Logger.out("Generating code ... ");
      try {
        JavaGenerator.run(dxdModel);
      } catch (Exception e) {
        Logger.verbose("");
        GeneratedFilePaths.getCodeFiles().forEach(filePath
            -> Logger.verbose(String.format("\t%s", filePath)));
        throw new RuntimeException(e);
      }
      Logger.log("[DONE]");
      GeneratedFilePaths.getCodeFiles().forEach(filePath
          -> Logger.verbose(String.format("\t%s", filePath)));
     }
  }
}
