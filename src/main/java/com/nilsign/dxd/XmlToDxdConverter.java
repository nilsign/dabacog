package com.nilsign.dxd;

import com.google.common.collect.ImmutableList;
import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdConfig;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelationType;
import com.nilsign.dxd.model.DxdFieldType;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.dxd.model.DxdSqlConnection;
import com.nilsign.dxd.types.CodeType;
import com.nilsign.dxd.types.DatabaseType;
import com.nilsign.misc.Wrapper;
import com.nilsign.reader.xml.model.XmlDxdModel;
import com.nilsign.reader.xml.model.config.XmlCodeConfig;
import com.nilsign.reader.xml.model.config.XmlDiagramsConfig;
import com.nilsign.reader.xml.model.config.XmlSqlConfig;
import com.nilsign.reader.xml.model.entities.XmlField;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public final class XmlToDxdConverter {

  public static DxdModel run(@NonNull XmlDxdModel xmlModel) {
    try {
      return DxdModel.of(
          xmlModel.getName(),
          buildDxdConfig(xmlModel),
          buildDxdClasses(xmlModel));
      } catch (Exception e) {
      throw new DxdModelException(e);
    }
  }

  private static DxdConfig buildDxdConfig(@NonNull XmlDxdModel xmlModel) {
    DxdConfig dxdConfig = new DxdConfig();
    buildDxdDiagramsConfig(xmlModel, dxdConfig);
    buildDxdSqlConfig(xmlModel, dxdConfig);
    buildDxdCodeConfig(xmlModel, dxdConfig);
    return dxdConfig;
  }

  private static void buildDxdDiagramsConfig(
      @NonNull XmlDxdModel xmlModel,
      @NonNull DxdConfig dxdConfig) {
    try {
      XmlDiagramsConfig diagramsConfig = xmlModel.getConfig().getDiagramsConfig();
      dxdConfig.setDiagramDatabaseOutputPath(diagramsConfig.getDiagramDatabaseOutputPath());
      dxdConfig.setDiagramDatabaseTitle(diagramsConfig.getDiagramDatabaseTitle());
      dxdConfig.setDiagramDatabasePrimaryKeyFieldPorts(
          diagramsConfig.isDiagramDatabasePrimaryKeyFieldPorts());
      dxdConfig.setDiagramDatabaseForeignKeyFieldPorts(
          diagramsConfig.isDiagramDatabaseForeignKeyFieldPorts());
    } catch (Exception e) {
      throw new RuntimeException(
          "Xml <config> <diagramsConfig ... </config> to Dxd config model conversion failed.",
          e);
    }
  }

  private static void buildDxdSqlConfig(
      @NonNull XmlDxdModel xmlModel,
      @NonNull DxdConfig dxdConfig) {
    try {
      XmlSqlConfig sqlConfig = xmlModel.getConfig().getSqlConfig();
      dxdConfig.setSqlDatabaseType(
          DatabaseType.valueOf(sqlConfig.getSqlDatabaseType().toUpperCase()));
      dxdConfig.setSqlOutputPath(sqlConfig.getSqlOutputPath());
      dxdConfig.setSqlGlobalSequence(sqlConfig.isSqlGlobalSequence());
      dxdConfig.setSqlDropSchema(sqlConfig.isSqlDropSchema());
      dxdConfig.setSqlConnections(buildDxdSqlConnections(xmlModel));
    } catch (Exception e) {
      throw new RuntimeException(
          "Xml <config> <sqlConfig ... </config> to Dxd config model conversion failed.",
          e);
    }
  }

  private static ImmutableList<DxdSqlConnection> buildDxdSqlConnections(
      @NonNull XmlDxdModel xmlModel) {
    try {
      return ImmutableList.copyOf(
          xmlModel.getConfig().getSqlConfig().getSqlConnections()
              .stream()
              .map(xmlSqlConnection
                  -> DxdSqlConnection.of(
                      xmlSqlConnection.getEnvironmentName(),
                      xmlSqlConnection.getUrl(),
                      xmlSqlConnection.getPort(),
                      xmlSqlConnection.getDatabaseName(),
                      xmlSqlConnection.getUser()))
              .collect(Collectors.toUnmodifiableList()));
      } catch (Exception e) {
      throw new RuntimeException(
          "Xml <config> <sqlConfig> <sqlConnection ... </config> to Dxd config model conversion "
              + "failed.",
          e);
    }
  }

  private static void buildDxdCodeConfig(
      @NonNull XmlDxdModel xmlModel,
      @NonNull DxdConfig dxdConfig) {
    try {
      XmlCodeConfig codeConfig = xmlModel.getConfig().getCodeConfig();
      dxdConfig.setCodeType(CodeType.valueOf(codeConfig.getCodeType().toUpperCase()));
      dxdConfig.setCodeOutputPath(codeConfig.getCodeOutputPath());
      dxdConfig.setCodePackageName(codeConfig.getCodePackageName());
      dxdConfig.setCodePasswordsFile(codeConfig.getCodePasswordsFile());
      dxdConfig.setCodePasswordsOutputPath(codeConfig.getCodePasswordsOutputPath());
    } catch (Exception e) {
      throw new RuntimeException(
          "Xml <config> <codeConfig ... </config> to Dxd config model conversion failed.",
          e);
    }
  }

  private static List<DxdClass> buildDxdClasses(@NonNull XmlDxdModel xmlModel) {
    final Wrapper<String> className = Wrapper.of();
    try {
      List<DxdClass> dxdClasses = new ArrayList<>();
      xmlModel.getEntities().getClasses().forEach(aClass -> {
        className.set(aClass.getName());
        List<DxdField> dxdFields = new ArrayList<>();
        aClass.getFields().forEach(field -> dxdFields.add(buildDxdField(field)));
        dxdClasses.add(DxdClass.of(aClass.getName(), ImmutableList.copyOf(dxdFields)));
      });
      return dxdClasses;
    } catch (Exception e) {
      throw new RuntimeException(
          String.format(
              "Xml <class name=\"%s\"> ... </class> to Dxd class model conversion failed.",
              className.get()),
          e);
    }
  }

  private static DxdField buildDxdField(@NonNull XmlField field) {
    try {
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
    } catch (Exception e) {
      throw new RuntimeException(
          String.format(
              "Xml <field name=\"%s\"> ... </field> to Dxd field model conversion failed.",
              field.getName()),
          e);
      }
  }
}
