package com.nilsign.generators.diagram;

import com.nilsign.dxd.elements.DxdModel;
import com.nilsign.dxd.elements.entities.DxdEntityClass;
import com.nilsign.dxd.elements.entities.DxdEntityClassField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

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
    File outputFile = super.createTargetFile();
    try (FileWriter writer = new FileWriter(outputFile)) {
      writer.write(new StringBuffer()
        .append(renderOpenGraph())
        .append(renderMetaGraph())
        .append(renderNodes())
        .append(renderEdges())
        .append(renderCloseGraph())
        .toString());
    } catch (IOException e) {
      throw new GraphGeneratorException(
          "An error occurred while writing to the database diagram description file.", e);
    }
  }

  private String renderOpenGraph() {
    return "digraph G {\n";
  }

  private String renderMetaGraph() {
    return new StringBuffer()
        .append("\tnode [shape=plaintext fontname=\"Arial\" fontsize=\"10\"]\n")
        .append(String.format("\tlabel = \"%s\";\n", "Database Diagram"))
        .append("\tlabelloc = \"t\";\n")
        .toString();
  }

  private String renderNodes() {
    StringBuffer output = new StringBuffer();
    super.dxdModel.getEntities().getDxdClasses().forEach(dxdEntityClass -> {
      output.append(renderNode(dxdEntityClass));
    });
    // output.append(generateNodesManyToMany());
    return output.toString();
  }

  private String renderNode(DxdEntityClass dxdClass) {
    StringBuffer output = new StringBuffer();
    output
        .append(String.format("\ttable_node_%s [\n", dxdClass.getName()))
        .append("\t\tlabel=<\n")
        .append("\t\t\t<table border=\"1\" cellborder=\"1\" cellspacing=\"1\">\n")
        .append(String.format(
            "\t\t\t\t<tr><td colspan=\"6\">%s</td></tr>\n",
            dxdClass.getName()))
        .append("\t\t\t\t\t<tr>\n")
        .append("\t\t\t\t\t\t<td>FK</td>\n")
        .append("\t\t\t\t\t\t<td>NAME</td>\n")
        .append("\t\t\t\t\t\t<td>TYPE</td>\n")
        .append("\t\t\t\t\t\t<td>INDEXED</td>\n")
        .append("\t\t\t\t\t\t<td>UNIQUE</td>\n")
        .append("\t\t\t\t\t\t<td>NULLABLE</td>\n")
        .append("\t\t\t\t\t</tr>\n")
        .append(renderFields(Set.copyOf(dxdClass.getFields())))
        .append("\t\t\t</table>\n")
        .append("\t\t>\n")
        .append("\t]\n\n");

    return output.toString();
  }

  private void generateNodesManyToMany() {
  }

  private String renderFields(Set<DxdEntityClassField> fields) {
    StringBuffer output = new StringBuffer();
    fields.forEach(
        field -> {
          output
              .append("\t\t\t\t\t<tr>\n")
              .append(String.format("\t\t\t\t\t\t<td>%s</td>\n", "todo"))
              .append(String.format("\t\t\t\t\t\t<td>%s</td>\n", field.getName()))
              .append(String.format("\t\t\t\t\t\t<td>%s</td>\n", field.getType()))
              .append(String.format("\t\t\t\t\t\t<td>%s</td>\n", "todo"))
              .append(String.format("\t\t\t\t\t\t<td>%s</td>\n", "todo"))
              .append(String.format("\t\t\t\t\t\t<td>%s</td>\n", "todo"))
              .append("\t\t\t\t\t</tr>\n");
        });
    return output.toString();
  }

  private String renderEdges() {
    // Render many-to-many
    return "";
  }

  private String renderEdge() {
    return "";
  }

  private String  renderCloseGraph() {
    return "}\n";
  }
}
