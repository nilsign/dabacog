package com.nilsign.generators.diagrams;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Graphml {

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public enum PortLocation {
    TOP("top");

    @Getter
    private final String shortName;
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public enum PortAlignment {
    NORTH("n"),
    EAST("e"),
    WEST("w");

    @Getter
    private final String shortName;
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public enum EdgeStyle {
    NORMAL(""),
    DASHED("[style=\"dashed\"]"),
    DOTTED("[style=\"dotted\"]");

    @Getter
    private final String styleName;
  }

  public static String openGraph() {
    return "digraph G {\n";
  }

  public static String addGraphProperties(@NonNull String graphName) {
    return new StringBuffer()
        .append("\tgraph "
            + "[pad=\"0.5\", nodesep=\"2\", ranksep=\"1.5\", ordering=\"in\"];\n")
        .append("\tnode [shape=plaintext fontname=\"Arial\" fontsize=\"12\"];\n")
        .append("\trankdir=\"LT\";\n")
        .append(String.format("\tlabel = \"%s\";\n", graphName))
        .append("\tlabelloc = \"t\";\n")
        .toString();
  }

  public static String openNode(@NonNull String nodeName) {
    return String.format("\tnode_%s [\n", nodeName);
  }

  public static String openNodeLabel() {
    return "\t\tlabel=<\n";
  }

  public static String openNodeLabelTable() {
    return "\t\t\t<table border=\"1\" cellborder=\"1\" cellspacing=\"1\">\n";
  }

  public static String addNodeLabelTableName(@NonNull String tableName, @NonNull int columns) {
    return String.format(
        "\t\t\t\t<tr><td colspan=\"%s\" port=\"port_%s\">%s</td></tr>\n",
        columns,
        PortLocation.TOP.getShortName(),
        tableName);
  }

  public static String addNodeLabelTableColumnNames(@NonNull List<String> columnNames) {
    StringBuffer output = new StringBuffer().append("\t\t\t\t<tr>\n");
    columnNames.forEach(columnName
        -> output.append(String.format("\t\t\t\t\t<td>%s</td>\n", columnName)));
    output.append("\t\t\t\t</tr>\n");
    return output.toString();
  }

  public static String openNodeLabelTableRow() {
    return "\t\t\t\t<tr>\n";
  }

  public static String addNodeLabelTableCell(@NonNull String value) {
    return String.format("\t\t\t\t\t<td>%s</td>\n", value);
  }

  public static String addNodeLabelTableCell(@NonNull String value, @NonNull String portName) {
    return String.format("\t\t\t\t\t<td port=\"%s\">%s</td>\n", portName, value);
  }

  public static String closeNodeLabelTableRow() {
    return "\t\t\t\t</tr>\n";
  }

  public static String addEdge(
      @NonNull String sourceNodeName,
      @NonNull String sourceNodePortName,
      @NonNull Graphml.PortAlignment sourceNodePortLocation,
      @NonNull String targetNodeName,
      @NonNull String targetNodePortName,
      @NonNull Graphml.PortAlignment targetNodePortLocation) {
    return String.format("\t%s:%s:%s -> %s:%s:%s\n",
        sourceNodeName,
        sourceNodePortName,
        sourceNodePortLocation.getShortName(),
        targetNodeName,
        targetNodePortName,
        targetNodePortLocation.getShortName());
  }

  public static String addEdge(
      @NonNull String sourceNodeName,
      @NonNull String sourceNodePortName,
      @NonNull Graphml.PortAlignment sourceNodePortLocation,
      @NonNull String targetNodeName,
      @NonNull String targetNodePortName,
      @NonNull Graphml.PortAlignment targetNodePortLocation,
      @NonNull EdgeStyle edgeStyle) {
    return String.format("\t%s:%s:%s -> %s:%s:%s %s\n",
        sourceNodeName,
        sourceNodePortName,
        sourceNodePortLocation.getShortName(),
        targetNodeName,
        targetNodePortName,
        targetNodePortLocation.getShortName(),
        edgeStyle.getStyleName());
  }

  public static String closeNodeLabelTable() {
    return "\t\t\t</table>\n";
  }

  public static String closeNodeLabel() {
    return "\t\t>\n";
  }

  public static String closeNode() {
    return "\t];\n\n";
  }

  public static String closeGraph() {
    return "}\n";
  }
}
