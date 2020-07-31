package com.nilsign.generators.diagrams.database.dot;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.Generator;
import lombok.NonNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class DotDatabaseDiagramGenerator extends Generator {

  public static final String OUTPUT_FILE_NAME = "dabacog-db-diagram.pot";

  private DotDatabaseDiagramGenerator(@NonNull DxdModel dxdModel) {
    super(dxdModel);
  }

  private static DotDatabaseDiagramGenerator of(@NonNull DxdModel dxdModel) {
    return new DotDatabaseDiagramGenerator(dxdModel);
  }

  public static void run(@NonNull DxdModel model) throws DotGeneratorException {
    DotDatabaseDiagramGenerator.of(model).generate();
  }

  @Override
  protected String getOutputDirectory() {
    return dxdModel.getConfig().getDiagramDatabaseOutputPath();
  }

  @Override
  protected String getOutputFileName() {
    return OUTPUT_FILE_NAME;
  }

  public void generate() throws DotGeneratorException {
    File outputFile = super.createOutputFile();
    try (FileWriter writer = new FileWriter(outputFile)) {
      writer.write(
          new StringBuffer()
              .append(Dot.openGraph())
              .append(Dot.addGraphProperties(dxdModel
                  .getConfig()
                  .getDiagramDatabaseTitle()))
              .append(addDatabaseEntityNodes())
              .append(addDatabaseEntityRelationEdges())
              .append(Dot.closeGraph())
              .toString());
    } catch (IOException e) {
      throw new DotGeneratorException(
          "An error occurred while writing to the database diagram description file.", e);
    }
  }

  private String addDatabaseEntityNodes() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getClasses().forEach(dxdEntityClass
        -> output.append(DotDatabaseNodeBuilder.buildEntityNode(dxdModel, dxdEntityClass)));
    super.dxdModel.getDistinctManyToManyRelations().forEach(dxdRelation
        -> output.append(DotDatabaseNodeBuilder.buildEntityRelationNode(dxdModel, dxdRelation)));
    return output.toString();
  }

  private String addDatabaseEntityRelationEdges() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getDistinctRelations().forEach(dxdRelation
        -> output.append(DotDatabaseEdgeBuilder.buildEntityRelationEdge(dxdRelation)));
    return output.toString();
  }
}