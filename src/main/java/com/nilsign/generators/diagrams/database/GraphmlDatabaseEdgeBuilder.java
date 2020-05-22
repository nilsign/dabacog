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
    }
    return null;
  }

  private String buildManyToManyEdges(@NonNull DxdEntityRelation dxdRelation) {
    return new StringBuffer()
        .append(Graphml.addEdge(
            String.format("node_%s", SqlSchemaGenerator.buildTableName(dxdRelation)),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
            Graphml.PortLocation.WEST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Graphml.PortLocation.WEST))
        .append(Graphml.addEdge(
            String.format("node_%s", SqlSchemaGenerator.buildTableName(dxdRelation)),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
            Graphml.PortLocation.EAST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Graphml.PortLocation.WEST))
        .toString();
  }

  private String buildManyToOneEdge(@NonNull DxdEntityRelation dxdRelation) {
    return Graphml.addEdge(
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
        String.format("port_%s",
            SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
        Graphml.PortLocation.EAST,
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
        String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
        Graphml.PortLocation.WEST,
        Graphml.EdgeStyle.DASHED);
  }

  private String buildOneToManyEdge(@NonNull DxdEntityRelation dxdRelation) {
    return Graphml.addEdge(
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
        String.format("port_%s",
            SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
        Graphml.PortLocation.EAST,
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
        String.format("port_%s",
            SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
        Graphml.PortLocation.WEST,
        Graphml.EdgeStyle.DASHED);
  }

  private String buildOneToOneEdge(@NonNull DxdEntityRelation dxdRelation) {
    StringBuffer output = new StringBuffer()
        .append(Graphml.addEdge(
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
            Graphml.PortLocation.EAST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
            String.format("port_%s",
                SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Graphml.PortLocation.WEST,
            Graphml.EdgeStyle.DOTTED));
    if (dxdRelation.hasBackReferencingField()
        && !dxdRelation.getBackReferencingField().isHidden()) {
      output.append(Graphml.addEdge(
          String.format("node_%s",
              SqlSchemaGenerator.buildTableName(dxdRelation.getReferencedClass())),
          String.format("port_%s",
              SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
          Graphml.PortLocation.EAST,
          String.format("node_%s",
              SqlSchemaGenerator.buildTableName(dxdRelation.getReferencingClass())),
          String.format("port_%s",
              SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
          Graphml.PortLocation.WEST,
          Graphml.EdgeStyle.DOTTED));
    }
    return output.toString();
  }
}
