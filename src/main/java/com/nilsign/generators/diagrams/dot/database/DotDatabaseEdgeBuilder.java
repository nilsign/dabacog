package com.nilsign.generators.diagrams.dot.database;

import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.generators.database.SqlSchemaGenerator;
import com.nilsign.generators.diagrams.dot.Dot;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotDatabaseEdgeBuilder {

  public static String buildEntityRelationEdge(@NonNull DxdFieldRelation dxdRelation) {
    switch(dxdRelation.getType()) {
      case MANY_TO_MANY: return buildManyToManyEdges(dxdRelation);
      case MANY_TO_ONE: return buildManyToOneEdge(dxdRelation);
      case ONE_TO_MANY: return buildOneToManyEdge(dxdRelation);
      case ONE_TO_ONE: return buildOneToOneEdge(dxdRelation);
      default: return null;
    }
  }

  private static String buildManyToManyEdges(@NonNull DxdFieldRelation dxdRelation) {
    if (!dxdRelation.isManyToMany()) {
      return "";
    }
    return new StringBuffer()
        .append(Dot.addEdge(
            String.format("node_%s", SqlSchemaGenerator.buildTableName(dxdRelation)),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
            Dot.PortAlignment.WEST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Dot.PortAlignment.WEST))
        .append(Dot.addEdge(
            String.format("node_%s", SqlSchemaGenerator.buildTableName(dxdRelation)),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
            Dot.PortAlignment.EAST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Dot.PortAlignment.WEST))
        .toString();
  }

  private static String buildOneToManyEdge(@NonNull DxdFieldRelation dxdRelation) {
    if (!dxdRelation.isOneToMany()) {
      return "";
    }
    return Dot.addEdge(
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
        String.format("port_%s",
            SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
        Dot.PortAlignment.EAST,
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
        String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
        Dot.PortAlignment.WEST,
        Dot.EdgeStyle.DASHED);
  }

  private static String buildManyToOneEdge(@NonNull DxdFieldRelation dxdRelation) {
    if (!dxdRelation.isManyToOne()) {
      return "";
    }
    return Dot.addEdge(
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
        String.format("port_%s",
            SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
        Dot.PortAlignment.EAST,
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
        String.format("port_%s",
            SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
        Dot.PortAlignment.WEST,
        Dot.EdgeStyle.DASHED);
  }

  private static String buildOneToOneEdge(@NonNull DxdFieldRelation dxdRelation) {
    if (!dxdRelation.isOneToOne()) {
      return "";
    }
    if (dxdRelation.isSelfReference()) {
      return Dot.addEdge(
          String.format(
            "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
          String.format(
              "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
          Dot.PortAlignment.EAST,
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
          String.format("port_%s", Dot.PortLocation.TOP.getShortName()),
          Dot.PortAlignment.NORTH,
          Dot.EdgeStyle.DOTTED);
    }
    StringBuffer output = new StringBuffer();
    if (dxdRelation.isOneToOne()) {
      output.append(Dot.addEdge(
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
          String.format(
              "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
          Dot.PortAlignment.EAST,
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
          String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
          Dot.PortAlignment.WEST,
          Dot.EdgeStyle.DOTTED));
      if (dxdRelation.isBiDirectional()) {
        output.append(Dot.addEdge(
            String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
              String.format(
                  "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
              Dot.PortAlignment.EAST,
              String.format(
                  "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
              String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
              Dot.PortAlignment.WEST,
              Dot.EdgeStyle.DOTTED));
      }
    }
    return output.toString();
  }
}
