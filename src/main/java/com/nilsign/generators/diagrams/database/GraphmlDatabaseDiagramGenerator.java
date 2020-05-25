package com.nilsign.generators.diagrams.database;

import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.generators.Generator;
import com.nilsign.generators.diagrams.Graphml;
import com.nilsign.generators.diagrams.GraphmlGeneratorException;
import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GraphmlDatabaseDiagramGenerator extends Generator {

  public static final String OUTPUT_FILE_NAME = "dabacog-db-diagram.pot";

  public static void run(@NonNull DxdModel model) throws GraphmlGeneratorException {
     new GraphmlDatabaseDiagramGenerator(model).generate();
  }

  private GraphmlDatabaseDiagramGenerator(@NonNull DxdModel dxdModel) {
    super(dxdModel);
  }

  @Override
  protected String getOutputDirectory() {
    return dxdModel.getMeta().getMetaDiagrams().getOutputPath();
  }

  @Override
  protected String getOutputFileName() {
    return OUTPUT_FILE_NAME;
  }

  public void generate() throws GraphmlGeneratorException {
    File outputFile = super.createOutputFile();
    try (FileWriter writer = new FileWriter(outputFile)) {
      writer.write(
          new StringBuffer()
              .append(Graphml.openGraph())
              .append(Graphml.addGraphProperties(dxdModel
                  .getMeta()
                  .getMetaDiagrams()
                  .getDxdMetaDiagramsDatabase()
                  .getDiagramName()))
              .append(addDatabaseEntityNodes())
              .append(addDatabaseEntityRelationEdges())
              .append(Graphml.closeGraph())
              .toString());
    } catch (IOException e) {
      throw new GraphmlGeneratorException(
          "An error occurred while writing to the database diagram description file.", e);
    }
  }

  private String addDatabaseEntityNodes() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getEntities().getDxdClasses().forEach(dxdEntityClass -> {
      output.append(GraphmlDatabaseNodeBuilder.buildEntityNode(dxdEntityClass));
    });
    super.dxdModel.getEntities().getManyToManyRelations().forEach(dxdRelation ->
       output.append(GraphmlDatabaseNodeBuilder.buildEntityRelationNode(dxdRelation)));
    return output.toString();
  }

  private String addDatabaseEntityRelationEdges() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getEntities().getRelations().forEach(dxdRelation ->
        output.append(GraphmlDatabaseEdgeBuilder.buildEntityRelationEdge(dxdRelation)));
    return output.toString();
  }
}
