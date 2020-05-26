package com.nilsign.generators.diagrams.database;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.generators.database.SqlSchemaGenerator;
import com.nilsign.generators.diagrams.Graphml;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public class GraphmlDatabaseEdgeBuilder {

  public static String buildEntityRelationEdge(@NonNull DxdEntityRelation dxdRelation) {
    switch(dxdRelation.getType()) {
      case MANY_TO_MANY: return of().buildManyToManyEdges(dxdRelation);
      case MANY_TO_ONE: return of().buildManyToOneEdge(dxdRelation);
      case ONE_TO_MANY: return of().buildOneToManyEdge(dxdRelation);
      case ONE_TO_ONE: return of().buildOneToOneEdge(dxdRelation);
      default: return null;
    }
  }

  private String buildManyToManyEdges(@NonNull DxdEntityRelation dxdRelation) {
    if (!dxdRelation.isManyToMany()) {
      return "";
    }
    return new StringBuffer()
        .append(Graphml.addEdge(
            String.format("node_%s", SqlSchemaGenerator.buildTableName(dxdRelation)),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
            Graphml.PortAlignment.WEST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Graphml.PortAlignment.WEST))
        .append(Graphml.addEdge(
            String.format("node_%s", SqlSchemaGenerator.buildTableName(dxdRelation)),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
            Graphml.PortAlignment.EAST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Graphml.PortAlignment.WEST))
        .toString();
  }

  private String buildManyToOneEdge(@NonNull DxdEntityRelation dxdRelation) {
    if (!dxdRelation.isManyToOne()) {
      return "";
    }
    return Graphml.addEdge(
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
        String.format("port_%s",
            SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
        Graphml.PortAlignment.EAST,
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
        String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
        Graphml.PortAlignment.WEST,
        Graphml.EdgeStyle.DASHED);
  }

  private String buildOneToManyEdge(@NonNull DxdEntityRelation dxdRelation) {
    if (!dxdRelation.isOneToMany()) {
      return "";
    }
    return Graphml.addEdge(
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
        String.format("port_%s",
            SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
        Graphml.PortAlignment.EAST,
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
        String.format("port_%s",
            SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
        Graphml.PortAlignment.WEST,
        Graphml.EdgeStyle.DASHED);
  }

  private String buildOneToOneEdge(@NonNull DxdEntityRelation dxdRelation) {
    if (!dxdRelation.isOneToOne()) {
      return "";
    }
    if (dxdRelation.isSelfReference()) {
      return Graphml.addEdge(
          String.format(
            "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
          String.format(
              "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
          Graphml.PortAlignment.EAST,
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
          String.format("port_%s", Graphml.PortLocation.TOP.getShortName()),
          Graphml.PortAlignment.NORTH,
          Graphml.EdgeStyle.DOTTED);
    }
    StringBuffer output = new StringBuffer();
    if (!dxdRelation.getReferencingField().isHidden()) {
      output.append(Graphml.addEdge(
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
          String.format(
              "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
          Graphml.PortAlignment.EAST,
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
          String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
          Graphml.PortAlignment.WEST,
          Graphml.EdgeStyle.DOTTED));
    }
    if (dxdRelation.hasBackReferencingField()
        && !dxdRelation.getBackReferencingField().isHidden()
        && !dxdRelation.isSelfReference()) {
      output.append(Graphml.addEdge(
          String.format("node_%s",
              SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
          String.format("port_%s",
              SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
          Graphml.PortAlignment.EAST,
          String.format("node_%s",
              SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
          String.format("port_%s",
              SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
          Graphml.PortAlignment.WEST,
          Graphml.EdgeStyle.DOTTED));
    }
    return output.toString();
  }
}
