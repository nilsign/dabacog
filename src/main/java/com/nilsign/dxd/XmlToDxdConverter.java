package com.nilsign.dxd;

import com.google.common.collect.ImmutableList;
import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdConfig;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelationType;
import com.nilsign.dxd.model.DxdFieldType;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.reader.xml.model.XmlModel;
import com.nilsign.reader.xml.model.config.XmlDiagramsConfig;
import com.nilsign.reader.xml.model.entities.XmlField;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public final class XmlToDxdConverter {

  public static DxdModel run(@NonNull XmlModel xmlModel) {
    return DxdModel.of(
        xmlModel.getName(),
        buildDxdConfig(xmlModel),
        buildDxdClasses(xmlModel));
  }

  private static DxdConfig buildDxdConfig(@NonNull XmlModel xmlModel) {
    XmlDiagramsConfig diagramsConfig = xmlModel.getConfig().getDiagramsConfig();
    DxdConfig dxdConfig = new DxdConfig();
    if (diagramsConfig.getDiagramDatabaseOutputPath() != null) {
      dxdConfig.setDiagramDatabaseOutputPath(diagramsConfig.getDiagramDatabaseOutputPath());
    }
    if (diagramsConfig.getDiagramDatabaseTitle() != null) {
      dxdConfig.setDiagramDatabaseTitle(diagramsConfig.getDiagramDatabaseTitle());
    }
    dxdConfig.setDiagramDatabasePrimaryKeyFieldPorts(
        diagramsConfig.isDiagramDatabasePrimaryKeyFieldPorts());
    dxdConfig.setDiagramDatabaseForeignKeyFieldPorts(
        diagramsConfig.isDiagramDatabaseForeignKeyFieldPorts());
    return dxdConfig;
  }

  private static List<DxdClass> buildDxdClasses(@NonNull XmlModel xmlModel) {
    List<DxdClass> dxdClasses = new ArrayList<>();
    xmlModel.getEntities().getClasses().forEach(aClass -> {
      List<DxdField> dxdFields = new ArrayList<>();
      aClass.getFields().forEach(field -> dxdFields.add(buildDxdField(field)));
      dxdClasses.add(DxdClass.of(aClass.getName(), ImmutableList.copyOf(dxdFields)));
    });
    return dxdClasses;
  }

  private static DxdField buildDxdField(@NonNull XmlField field) {
    return DxdField.of(
        DxdFieldType.of(field.getType()),
        DxdFieldType.of(field.getType()).isObject()
            ? DxdFieldType.of(field.getType()).getObjectName()
            : field.getName(),
        DxdFieldRelationType.of(field.getRelation()),
        field.isIndexed(),
        field.isUnique(),
        field.isNullable(),
        field.isFts(),
        field.getDefaultValue());
  }
}
