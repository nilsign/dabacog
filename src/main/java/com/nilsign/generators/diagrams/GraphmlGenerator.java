package com.nilsign.generators.diagrams;

import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.generators.Generator;
import lombok.NonNull;

import java.util.List;

public abstract class GraphmlGenerator extends Generator {

  protected GraphmlGenerator(DxdModel dxdModel) {
    super(dxdModel);
  }

  protected String openGraphml() {
    return "digraph G {\n";
  }

  protected String addGraphmlMetaDescription(@NonNull String graphName) {
    return new StringBuffer()
        .append("\tnode [shape=plaintext fontname=\"Arial\" fontsize=\"10\"]\n")
        .append(String.format("\tlabel = \"%s\";\n", graphName))
        .append("\tlabelloc = \"t\";\n")
        .toString();
  }

  protected String openGraphmlNode(@NonNull String nodeName) {
    return String.format("\tgml_node_%s [\n", nodeName);
  }

  protected String openGraphmlLabel() {
    return "\t\tlabel=<\n";
  }

  protected String openGraphmlTable() {
   return "\t\t\t<table border=\"1\" cellborder=\"1\" cellspacing=\"1\">\n";
  }

  protected String closeGraphmlTable() {
    return "\t\t\t</table>\n";
  }

  protected String addGraphmlTableName(@NonNull String tableName, @NonNull int columns) {
    return String.format(
        "\t\t\t\t<tr><td colspan=\"%s\">%s</td></tr>\n",
        columns,
        tableName);
  }

  protected String addGraphmlTableColumnNames(@NonNull List<String> columnNames) {
    StringBuffer output = new StringBuffer().append("\t\t\t\t<tr>\n");
    columnNames.forEach(columnName
        -> output.append(String.format("\t\t\t\t\t<td>%s</td>\n", columnName)));
    output.append("\t\t\t\t</tr>\n");
    return output.toString();
  }

  protected String addGraphmlTableRows(@NonNull List<List<String>> rowValues) {
    StringBuffer output = new StringBuffer();
    rowValues.forEach(row -> {
      output.append("\t\t\t\t<tr>\n");
      row.forEach(value
          -> output.append(String.format("\t\t\t\t\t<td>%s</td>\n", value)));
      output.append("\t\t\t\t</tr>\n");
    });
    return output.toString();
  }

  protected String closeGraphmlLabel() {
    return "\t\t>\n";
  }

  protected String closeGraphmlNode() {
    return "\t];\n\n";
  }

  protected String closeGraphmlGraph() {
    return "}\n";
  }
}
