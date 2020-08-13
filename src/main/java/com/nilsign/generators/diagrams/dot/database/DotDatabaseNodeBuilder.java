package com.nilsign.generators.diagrams.dot.database;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.database.Sql;
import com.nilsign.generators.diagrams.dot.Dot;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotDatabaseNodeBuilder {

  public static String buildEntityNode(
      @NonNull DxdModel dxdModel,
      @NonNull DxdClass dxdClass) {
    return new StringBuffer()
        .append(Dot.openNode(Sql.buildTableName(dxdClass)))
        .append(DotDatabaseNodeLabelBuilder.buildEntityNodeLabel(dxdModel, dxdClass))
        .append(Dot.closeNode())
        .toString();
  }

  public static String buildEntityRelationNode(
      @NonNull DxdModel dxdModel,
      @NonNull DxdFieldRelation dxdRelation) {
    return new StringBuffer()
        .append(Dot.openNode(Sql.buildTableName(dxdRelation)))
        .append(DotDatabaseNodeLabelBuilder.buildEntityRelationNodeLabel(dxdModel, dxdRelation))
        .append(Dot.closeNode())
        .toString();
  }
}
