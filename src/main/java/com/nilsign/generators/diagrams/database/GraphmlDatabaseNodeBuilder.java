package com.nilsign.generators.diagrams.database;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.generators.database.SqlSchemaGenerator;
import com.nilsign.generators.diagrams.Graphml;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public class GraphmlDatabaseNodeBuilder {

  public static String buildEntityNode(@NonNull DxdEntityClass dxdClass) {
    return of().buildEntityNodeImpl(dxdClass);
  }

  public static String buildEntityRelationNode(@NonNull DxdEntityRelation dxdRelation) {
    return of().buildEntityRelationNodeImpl(dxdRelation);
  }

  private String buildEntityNodeImpl(@NonNull DxdEntityClass dxdClass) {
    return new StringBuffer()
        .append(Graphml.openNode(SqlSchemaGenerator.buildTableName(dxdClass)))
        .append(GraphmlDatabaseNodeLabelBuilder.buildEntityNodeLabel(dxdClass))
        .append(Graphml.closeNode())
        .toString();
  }

  private String buildEntityRelationNodeImpl(@NonNull DxdEntityRelation dxdRelation) {
    return new StringBuffer()
        .append(Graphml.openNode(SqlSchemaGenerator.buildTableName(dxdRelation)))
        .append(GraphmlDatabaseNodeLabelBuilder.buildEntityRelationNodeLabel(dxdRelation))
        .append(Graphml.closeNode())
        .toString();
  }
}
