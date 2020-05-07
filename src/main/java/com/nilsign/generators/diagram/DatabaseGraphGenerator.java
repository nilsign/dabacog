package com.nilsign.generators.diagram;

import com.nilsign.dxd.elements.DxdModel;

public class DatabaseGraphGenerator extends GraphGenerator  {

  private static final String GRAPHVIZ_DOT_FILE_NAME = "database-entity-relation-diagram.pot";

  public static void run(DxdModel model) throws GraphGeneratorException {
    new DatabaseGraphGenerator(model).generate();
  }

  private DatabaseGraphGenerator(DxdModel dxdModel) {
    super(dxdModel);
  }

  @Override
  protected String getOutputDirectory() {
     return dxdModel.getDxdMeta().getDxdMetaDiagrams().getOutputPath();
  }

  @Override
  protected String getTargetFileName() {
    return GRAPHVIZ_DOT_FILE_NAME;
  }

  @Override
  public void generate() throws GraphGeneratorException {
    super.createTargetFile();
  }
}
