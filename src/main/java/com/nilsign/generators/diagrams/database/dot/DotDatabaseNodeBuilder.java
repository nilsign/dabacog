package com.nilsign.generators.diagrams.database.dot;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.database.SqlSchemaGenerator;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotDatabaseNodeBuilder {

  public static String buildEntityNode(
      @NonNull DxdModel dxdModel,
      @NonNull DxdClass dxdClass) {
    return new StringBuffer()
        .append(Dot.openNode(SqlSchemaGenerator.buildTableName(dxdClass)))
        .append(DotDatabaseNodeLabelBuilder.buildEntityNodeLabel(dxdModel, dxdClass))
        .append(Dot.closeNode())
        .toString();
  }

  public static String buildEntityRelationNode(
      @NonNull DxdModel dxdModel,
      @NonNull DxdFieldRelation dxdRelation) {
    return new StringBuffer()
        .append(Dot.openNode(SqlSchemaGenerator.buildTableName(dxdRelation)))
        .append(DotDatabaseNodeLabelBuilder.buildEntityRelationNodeLabel(dxdModel, dxdRelation))
        .append(Dot.closeNode())
        .toString();
  }
}
