package com.nilsign.generators.diagrams.dot.renderer;

import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.GeneratedFilePaths;
import com.nilsign.generators.Generator;
import com.nilsign.generators.diagrams.dot.database.DotDatabaseDiagramGenerator;
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

  public static void run(@NonNull DxdModel dxdModel) {
    try {
      GraphvizDotRenderer.of(dxdModel).render();
    } catch (Exception e) {
      throw new GraphvizDotRendererException(e);
    }
  }

  private void render() {
    File outputFile = super.createOutputFile();
    Graphviz graphviz;
    try {
      graphviz = Graphviz.fromFile(getInputFile());
    } catch (Exception e) {
      throw new RuntimeException(
          String.format("Failed to read Dot input file '%s'.", getInputFile()),
          e);
     }
     try {
        graphviz.render(Format.PNG).toFile(outputFile);
      } catch (Exception e) {
      throw new RuntimeException(
          String.format("Failed to render into target file '%s'.", outputFile),
          e);
    }
    GeneratedFilePaths.setDatabaseDiagramFile(outputFile.getAbsolutePath());
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
