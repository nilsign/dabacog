package com.nilsign.generators.diagrams.database.dot;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.dxd.model.DxdFieldRelationType;
import com.nilsign.dxd.model.DxdFieldType;
import com.nilsign.dxd.model.DxdModel;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public class DotDatabaseNodeLabelBuilder {

  public static String buildEntityNodeLabel(
      @NonNull DxdModel dxdModel,
      @NonNull DxdClass dxdClass) {
    return of().buildEntityNodeLabelImpl(dxdModel, dxdClass);
  }

  public static String buildEntityRelationNodeLabel(
      @NonNull DxdModel dxdModel,
      @NonNull DxdFieldRelation dxdRelation) {
    return of().buildEntityRelationNodeLabelImpl(dxdModel, dxdRelation);
  }

  public String buildEntityNodeLabelImpl(
      @NonNull DxdModel dxdModel,
      @NonNull DxdClass dxdClass) {
    StringBuffer output = new StringBuffer()
        .append(Dot.openNodeLabel())
        .append(Dot.openNodeLabelTable())
        .append(DotDatabaseNodeLabelRowBuilder.buildEntityNodeNameRow(dxdClass))
        .append(DotDatabaseNodeLabelRowBuilder.buildEntityNodeColumnNamesRow())
        .append(DotDatabaseNodeLabelRowBuilder.buildEntityNodePrimaryKeyRow(dxdModel));
    // Adds database fields in case of one-directional one-to-many relations. The foreign key is
    // added to the according referenced table, and not in the referencing table.
    dxdModel.getManyToOneRelations()
        .stream()
        .filter(dxdFieldRelation
            -> dxdFieldRelation.isOneDirectional()
            && dxdFieldRelation.getSecondClass().equals(dxdClass))
        .forEach(dxdFieldRelation
            -> output.append(
                DotDatabaseNodeLabelRowBuilder.buildEntityNodeFieldRow(
                    dxdModel,
                    DxdField.of(
                        DxdFieldType.of(dxdFieldRelation.getFirstClass().getName()),
                        null,
                        DxdFieldRelationType.ONE_TO_MANY,
                        true,
                        true,
                        false,
                        false,
                        null))));
    dxdClass.getFields()
        .stream()
        .filter(field
            -> !field.hasRelation()
            || !field.getRelationType().isManyToMany()
            && !field.getRelationType().isManyToOne())
        .forEach(field
            -> output.append(
                DotDatabaseNodeLabelRowBuilder.buildEntityNodeFieldRow(dxdModel, field)));
    return output
        .append(Dot.closeNodeLabelTable())
        .append(Dot.closeNodeLabel())
        .toString();
  }

  public String buildEntityRelationNodeLabelImpl(
      @NonNull DxdModel dxdModel,
      @NonNull DxdFieldRelation dxdRelation) {
    return new StringBuffer()
        .append(Dot.openNodeLabel())
        .append(Dot.openNodeLabelTable())
        .append(DotDatabaseNodeLabelRowBuilder.buildEntityRelationNodeNameRow(dxdRelation))
        .append(DotDatabaseNodeLabelRowBuilder.buildEntityRelationNodeColumnNamesRow())
        .append(DotDatabaseNodeLabelRowBuilder.buildEntityRelationNodeFieldsRow(
            dxdModel,
            dxdRelation))
        .append(Dot.closeNodeLabelTable())
        .append(Dot.closeNodeLabel())
        .toString();
  }
}
