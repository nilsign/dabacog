package com.nilsign.generators.diagrams.database;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.Generator;
import com.nilsign.generators.diagrams.database.dot.DotDatabaseDiagramGenerator;
import com.nilsign.helper.FileHelper;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import lombok.NonNull;
import java.io.File;

public final class GraphvizDotRenderer extends Generator {

  private static final String TARGET_FILE_NAME = "dabacog-db-diagram.png";

  private GraphvizDotRenderer(@NonNull DxdModel dxdModel) {
    super(dxdModel);
  }

  private static GraphvizDotRenderer of(@NonNull DxdModel dxdModel) {
    return new GraphvizDotRenderer(dxdModel);
  }

  public static void run(@NonNull DxdModel dxdModel) throws GraphvizDotRendererException {
    GraphvizDotRenderer.of(dxdModel).run();
  }

  private void run() throws GraphvizDotRendererException {
    File outputFile;
    try {
      outputFile = super.createOutputFile();
    } catch (Exception e) {
      throw new GraphvizDotRendererException(
          String.format("Graphviz failed to create target file '%s'.", getOutputFilePath()), e);
    }
    try {
      Graphviz.fromFile(getInputFile())
          .render(Format.PNG)
          .toFile(outputFile);
    } catch (Exception e) {
      throw new GraphvizDotRendererException(
          String.format(
              "Graphviz failed to render diagram into target file '%s'.",
              getOutputFilePath()),
          e);
    }
  }

  @Override
  protected String getOutputDirectory() {
    return super.dxdModel.getConfig().getDiagramDatabaseOutputPath();
  }

  @Override
  protected String getOutputFileName() {
    return TARGET_FILE_NAME;
  }

  private File getInputFile() {
    return new File(String.format("%s%s",
        FileHelper.normalizePath(getOutputDirectory()),
        DotDatabaseDiagramGenerator.OUTPUT_FILE_NAME));
  }
}
