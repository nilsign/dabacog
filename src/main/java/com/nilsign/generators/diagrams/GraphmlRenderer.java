package com.nilsign.generators.diagrams;

import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.generators.Generator;
import com.nilsign.generators.diagrams.database.GraphmlDatabaseDiagramGenerator;
import com.nilsign.helper.FileHelper;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import lombok.NonNull;

import java.io.File;

public class GraphmlRenderer extends Generator {

  private static final String TARGET_FILE_NAME = "dabacog-db-diagram.png";

  protected GraphmlRenderer(@NonNull DxdModel dxdModel) {
    super(dxdModel);
  }

  public static void run(@NonNull DxdModel dxdModel) throws GraphmlRendererException {
    new GraphmlRenderer(dxdModel).render();
  }

  private void render() throws GraphmlRendererException {
    File outputFile;
    try {
      outputFile = super.createOutputFile();
    } catch (Exception e) {
      throw new GraphmlRendererException(
          String.format("Graphviz failed to create target file '%s'.", getOutputFilePath()), e);
    }
    try {
      Graphviz.fromFile(getInputFile())
          .render(Format.PNG)
          .toFile(outputFile);
    } catch (Exception e) {
      throw new GraphmlRendererException(
          String.format(
              "Graphviz failed to render diagram into target file '%s'.",
              getOutputFilePath()),
          e);
    }
  }

  @Override
  protected String getOutputDirectory() {
    return super.dxdModel.getMeta().getMetaDiagrams().getOutputPath();
  }

  @Override
  protected String getOutputFileName() {
    return TARGET_FILE_NAME;
  }

  private File getInputFile() {
    return new File(String.format("%s%s",
        FileHelper.normalizePath(getOutputDirectory()),
        GraphmlDatabaseDiagramGenerator.OUTPUT_FILE_NAME));
  }
}
