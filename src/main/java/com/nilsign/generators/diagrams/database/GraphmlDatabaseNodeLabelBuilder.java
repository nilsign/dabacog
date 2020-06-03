package com.nilsign.generators.diagrams.database;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.generators.diagrams.Graphml;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public class GraphmlDatabaseNodeLabelBuilder {

  public static String buildEntityNodeLabel(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityClass dxdClass) {
    return of().buildEntityNodeLabelImpl(dxdModel, dxdClass);
  }

  public static String buildEntityRelationNodeLabel(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityRelation dxdRelation) {
    return of().buildEntityRelationNodeLabelImpl(dxdModel, dxdRelation);
  }

  public String buildEntityNodeLabelImpl(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityClass dxdClass) {
    StringBuffer output = new StringBuffer()
        .append(Graphml.openNodeLabel())
        .append(Graphml.openNodeLabelTable())
        .append(GraphmlDatabaseNodeLabelRowBuilder.buildEntityNodeNameRow(dxdClass))
        .append(GraphmlDatabaseNodeLabelRowBuilder.buildEntityNodeColumnNamesRow())
        .append(GraphmlDatabaseNodeLabelRowBuilder.buildEntityNodePrimaryKeyRow(dxdModel));
    dxdClass.getFields()
        .stream()
        .filter(field
            -> !field.isRelation()
            || field.getRelation() != null
            && !field.getRelation().isManyToMany()
            && !(field.getRelation().isOneToMany() && field.isToManyRelation())
            && !(field.getRelation().isOneToOne() && field.isHidden()))
        .forEach(field
            -> output.append(
                GraphmlDatabaseNodeLabelRowBuilder.buildEntityNodeFieldRow(dxdModel, field)));
    return output
        .append(Graphml.closeNodeLabelTable())
        .append(Graphml.closeNodeLabel())
        .toString();
  }

  public String buildEntityRelationNodeLabelImpl(
      @NonNull DxdModel dxdModel,
      @NonNull DxdEntityRelation dxdRelation) {
    return new StringBuffer()
        .append(Graphml.openNodeLabel())
        .append(Graphml.openNodeLabelTable())
        .append(GraphmlDatabaseNodeLabelRowBuilder.buildEntityRelationNodeNameRow(dxdRelation))
        .append(
            GraphmlDatabaseNodeLabelRowBuilder.buildEntityRelationNodeColumnNamesRow())
        .append(GraphmlDatabaseNodeLabelRowBuilder.buildEntityRelationNodeFieldsRow(
            dxdModel,
            dxdRelation))
        .append(Graphml.closeNodeLabelTable())
        .append(Graphml.closeNodeLabel())
        .toString();
  }
}
