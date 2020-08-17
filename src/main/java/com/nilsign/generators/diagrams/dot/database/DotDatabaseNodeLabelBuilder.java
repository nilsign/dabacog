package com.nilsign.generators.diagrams.dot.database;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.dxd.model.DxdFieldRelationType;
import com.nilsign.dxd.model.DxdFieldType;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.diagrams.dot.Dot;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotDatabaseNodeLabelBuilder {

  public static String buildTableNodeLabel(
      @NonNull DxdModel model,
      @NonNull DxdClass aClass) {
    StringBuffer output = new StringBuffer()
        .append(Dot.openNodeLabel())
        .append(Dot.openNodeLabelTable())
        .append(DotDatabaseNodeLabelRowBuilder.buildTableNodeNameRow(aClass))
        .append(DotDatabaseNodeLabelRowBuilder.buildTableNodeColumnNamesRow())
        .append(DotDatabaseNodeLabelRowBuilder.buildTableNodePrimaryKeyRow(model));
    // Adds database fields in case of one-directional one-to-many relations. The foreign key is
    // added to the according referenced table, but not in the referencing table.
    model.getManyToOneRelations()
        .stream()
        .filter(relation
            -> relation.isOneDirectional()
            && relation.getSecondClass().equals(aClass))
        .forEach(relation
            -> output.append(
                DotDatabaseNodeLabelRowBuilder.buildTableNodeFieldRow(
                    model,
                    DxdField.of(
                        DxdFieldType.of(relation.getFirstClass().getName()),
                        null,
                        DxdFieldRelationType.ONE_TO_MANY,
                        true,
                        true,
                        false,
                        false,
                        null))));
    aClass.getFields()
        .stream()
        .filter(field
            -> !field.hasRelation()
            || !field.getRelationType().isManyToMany()
            && !field.getRelationType().isManyToOne())
        .forEach(field
            -> output.append(
                DotDatabaseNodeLabelRowBuilder.buildTableNodeFieldRow(model, field)));
    return output
        .append(Dot.closeNodeLabelTable())
        .append(Dot.closeNodeLabel())
        .toString();
  }

  public static String buildRelationalTableNodeLabel(
      @NonNull DxdModel model,
      @NonNull DxdFieldRelation relation) {
    return new StringBuffer()
        .append(Dot.openNodeLabel())
        .append(Dot.openNodeLabelTable())
        .append(DotDatabaseNodeLabelRowBuilder.buildRelationalTableNodeNameRow(relation))
        .append(DotDatabaseNodeLabelRowBuilder.buildRelationalTableNodeColumnNamesRow())
        .append(DotDatabaseNodeLabelRowBuilder.buildRelationalTableNodeFieldsRow(
            model,
            relation))
        .append(Dot.closeNodeLabelTable())
        .append(Dot.closeNodeLabel())
        .toString();
  }
}
