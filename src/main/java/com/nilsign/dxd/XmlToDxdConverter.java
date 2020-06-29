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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(staticName = "of")
public class XmlToDxdConverter {

  @NonNull private final XmlModel xmlModel;

  public DxdModel convert() {
    return DxdModel.of(
        xmlModel.getName(),
        buildDxdConfig(),
        buildDxdClasses());
  }

  private DxdConfig buildDxdConfig() {
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

  private List<DxdClass> buildDxdClasses() {
    List<DxdClass> dxdClasses = new ArrayList<>();
    xmlModel.getEntities().getClasses().forEach(aClass -> {
      List<DxdField> dxdFields = new ArrayList<>();
      aClass.getFields().forEach(field -> dxdFields.add(buildDxdField(field)));
      dxdClasses.add(DxdClass.of(aClass.getName(), ImmutableList.copyOf(dxdFields)));
    });
    return dxdClasses;
  }

  private DxdField buildDxdField(@NonNull XmlField field) {
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
