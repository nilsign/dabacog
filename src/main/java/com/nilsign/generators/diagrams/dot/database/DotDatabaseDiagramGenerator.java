package com.nilsign.generators.diagrams.dot.database;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.GeneratedFilePaths;
import com.nilsign.generators.Generator;
import com.nilsign.generators.diagrams.dot.Dot;
import lombok.NonNull;
import java.io.File;
import java.io.FileWriter;

public final class DotDatabaseDiagramGenerator extends Generator {

  public static final String OUTPUT_FILE_NAME = "DabacogDatabaseDiagram.pot";

  private DotDatabaseDiagramGenerator(@NonNull DxdModel model) {
    super(model);
  }

  private static DotDatabaseDiagramGenerator of(@NonNull DxdModel model) {
    return new DotDatabaseDiagramGenerator(model);
  }

  public static void run(@NonNull DxdModel model) {
    try {
      DotDatabaseDiagramGenerator.of(model).generate();
      } catch (Exception e) {
        throw new DotDatabaseDiagramGeneratorException(e);
    }
  }

  @Override
  protected String getOutputDirectory() {
    return super.model.getConfig().getDiagramDatabaseOutputPath();
  }

  @Override
  protected String getOutputFileName() {
    return OUTPUT_FILE_NAME;
  }

  private void generate() {
    File outputFile = super.createOutputFile();
    try (FileWriter writer = new FileWriter(outputFile)) {
      writer.write(
          new StringBuffer()
              .append(Dot.openGraph())
              .append(Dot.addGraphProperties(super.model
                  .getConfig()
                  .getDiagramDatabaseTitle()))
              .append(addDatabaseTableNodes())
              .append(addDatabaseTableRelationEdges())
              .append(Dot.closeGraph())
              .toString());
    } catch (Exception e) {
      throw new RuntimeException(
          String.format(
              "Failed to write into target file '%s'.",
              outputFile),
          e);
    }
    GeneratedFilePaths.setDatabaseDiagramDotFile(outputFile.getAbsolutePath());
  }

  private String addDatabaseTableNodes() {
    StringBuffer output = new StringBuffer();
    super.model.getClasses().forEach(aClass
        -> output.append(DotDatabaseNodeBuilder.buildTableNode(super.model, aClass)));
    super.model.getDistinctManyToManyRelations().forEach(relation
        -> output.append(DotDatabaseNodeBuilder.buildRelationalTableNode(super.model, relation)));
    return output.toString();
  }

  private String addDatabaseTableRelationEdges() {
    StringBuffer output = new StringBuffer();
    super.model.getDistinctRelations().forEach(relation
        -> output.append(DotDatabaseEdgeBuilder.buildTableRelationEdge(relation)));
    return output.toString();
  }
}
