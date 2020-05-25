package com.nilsign.generators.diagrams.database;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.generators.database.SqlSchemaGenerator;
import com.nilsign.generators.diagrams.Graphml;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public class GraphmlDatabaseNodeBuilder {

  public static String buildEntityNode(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityClass dxdClass) {
    return of().buildEntityNodeImpl(dxdModel, dxdClass);
  }

  public static String buildEntityRelationNode(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityRelation dxdRelation) {
    return of().buildEntityRelationNodeImpl(dxdModel, dxdRelation);
  }

  private String buildEntityNodeImpl(@NonNull DxdModel dxdModel, @NonNull DxdEntityClass dxdClass) {
    return new StringBuffer()
        .append(Graphml.openNode(SqlSchemaGenerator.buildTableName(dxdClass)))
        .append(GraphmlDatabaseNodeLabelBuilder.buildEntityNodeLabel(dxdModel, dxdClass))
        .append(Graphml.closeNode())
        .toString();
  }

  private String buildEntityRelationNodeImpl(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityRelation dxdRelation) {
    return new StringBuffer()
        .append(Graphml.openNode(SqlSchemaGenerator.buildTableName(dxdRelation)))
        .append(GraphmlDatabaseNodeLabelBuilder.buildEntityRelationNodeLabel(dxdModel, dxdRelation))
        .append(Graphml.closeNode())
        .toString();
  }
}
