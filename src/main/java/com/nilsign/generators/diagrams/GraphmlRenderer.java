package com.nilsign.generators.diagrams;

import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.generators.Generator;
import com.nilsign.helper.FileHelper;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import java.io.File;

public class GraphmlRenderer extends Generator {

  private static final String TARGET_FILE_NAME = "dabacog-db-diagram.png";

  protected GraphmlRenderer(DxdModel dxdModel) {
    super(dxdModel);
  }

  public static void run(DxdModel dxdModel) throws GraphmlRendererException {
    new GraphmlRenderer(dxdModel).render();
  }

  private void render() throws GraphmlRendererException {
    File targetFile;
    try {
      targetFile = super.createGenerationTargetFile();
    } catch (Exception e) {
      throw new GraphmlRendererException(
          String.format("Graphviz failed to create target file '%s'.", getTargetFilePath()), e);
    }
    try {
      String graphmlFile = String.format("%s%s",
          FileHelper.normalizePath(getOutputDirectory()),
          GraphmlDatabaseDiagramGenerator.TARGET_FILE_NAME);
      Graphviz.fromFile(new File(graphmlFile))
          .render(Format.PNG)
          .toFile(targetFile);
    } catch (Exception e) {
      throw new GraphmlRendererException(
          String.format(
              "Graphviz failed to render diagram into target file '%s'.",
              getTargetFilePath()),
          e);
    }
  }

  @Override
  protected String getOutputDirectory() {
    return super.dxdModel.getDxdMeta().getDxdMetaDiagrams().getOutputPath();
  }

  @Override
  protected String getTargetFileName() {
    return TARGET_FILE_NAME;
  }
}
