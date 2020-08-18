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

  public static String buildTableNode(
      @NonNull DxdModel model,
      @NonNull DxdClass aClass) {
    return new StringBuffer()
        .append(Dot.openNode(Sql.buildTableName(aClass)))
        .append(DotDatabaseNodeLabelBuilder.buildTableNodeLabel(model, aClass))
        .append(Dot.closeNode())
        .toString();
  }

  public static String buildRelationalTableNode(
      @NonNull DxdModel model,
      @NonNull DxdFieldRelation relation) {
    return new StringBuffer()
        .append(Dot.openNode(Sql.buildTableName(relation)))
        .append(DotDatabaseNodeLabelBuilder.buildRelationalTableNodeLabel(model, relation))
        .append(Dot.closeNode())
        .toString();
  }
}
