package com.nilsign.generators.code.java.entities;

import com.google.common.collect.Lists;
import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.code.java.Java;
import com.nilsign.generators.database.Sql;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.NonNull;

import javax.lang.model.element.Modifier;
import java.util.Date;
import java.util.List;

public final class JavaEntityClassBuilder {

  public static String buildEntityClass(@NonNull DxdModel model, @NonNull DxdClass aClass) {
    TypeSpec javaClass = TypeSpec.classBuilder(Java.normalizeClassName(aClass.getName()))
        .addFields(buildFieldSpecs(aClass.getFields()))
        .addMethods(buildMethodSpecs(aClass.getFields()))
        .build();
    return JavaFile
        .builder(model.getConfig().getCodePackageName(), javaClass)
        .build()
        .toString();
  }

  private static List<FieldSpec> buildFieldSpecs(@NonNull List<DxdField> fields) {
    List<FieldSpec> methodSpecs = Lists.newArrayList(
        buildPrimaryKeyFieldSpec());
    fields.forEach(field -> methodSpecs.add(
        buildFieldSpec(field)));
    return methodSpecs;
  }

  private static FieldSpec buildPrimaryKeyFieldSpec() {
    return FieldSpec
        .builder(
            TypeName.LONG,
            Java.normalizeFieldName(Sql.SQL_PRIMARY_KEY_NAME),
            Modifier.PRIVATE)
        .build();
    }

    private static FieldSpec buildFieldSpec(@NonNull DxdField field) {
      return FieldSpec
          .builder(
              field.getType().isObject()
                  ? ClassName.get("com.nilsign.dabacog.demo", field.getType().getObjectName())
                  : getJavaTypeName(field),
              Java.normalizeFieldName(field.getName()),
              Modifier.PRIVATE)
          .build();
    }

  private static List<MethodSpec> buildMethodSpecs(@NonNull List<DxdField> fields) {
    List<MethodSpec> methodSpecs = Lists.newArrayList(
        buildPrimaryKeyGetterMethodSpec(),
        buildPrimaryKeySetterMethodSpec());
    fields.forEach(field -> methodSpecs.addAll(List.of(
        buildGetterMethodSpec(field),
        buildSetterMethodSpec(field))));
    return methodSpecs;
  }

  private static MethodSpec buildPrimaryKeyGetterMethodSpec() {
    return MethodSpec
        .methodBuilder(String.format("get%s", Java.startUpperCased(Sql.SQL_PRIMARY_KEY_NAME)))
        .addModifiers(Modifier.PUBLIC)
        .addStatement(String.format("return %s", Java.normalizeFieldName(Sql.SQL_PRIMARY_KEY_NAME)))
        .returns(TypeName.LONG)
        .build();
  }

  private static MethodSpec buildPrimaryKeySetterMethodSpec() {
    String fieldName = Java.normalizeFieldName(Sql.SQL_PRIMARY_KEY_NAME);
    return MethodSpec.methodBuilder(
        String.format(
            "set%s", Java.startUpperCased(Sql.SQL_PRIMARY_KEY_NAME)))
        .addModifiers(Modifier.PUBLIC)
        .addParameter(TypeName.LONG, fieldName)
        .addStatement(String.format("this.%s = %s", fieldName, fieldName))
        .build();
  }

  private static MethodSpec buildGetterMethodSpec(@NonNull DxdField field) {
    return MethodSpec.methodBuilder(String.format("get%s", field.getName()))
        .addModifiers(Modifier.PUBLIC)
        .addStatement(String.format("return %s", Java.normalizeFieldName(field.getName())))
        .returns(getJavaTypeName(field))
        .build();
  }

  private static MethodSpec buildSetterMethodSpec(@NonNull DxdField field) {
    String fieldName = Java.normalizeFieldName(field.getName());
    return MethodSpec.methodBuilder(String.format("set%s", field.getName()))
        .addModifiers(Modifier.PUBLIC)
        .addParameter(getJavaTypeName(field), fieldName)
        .addStatement(String.format("this.%s = %s", field.getName(), field.getName()))
        .build();
  }

  private static TypeName getJavaTypeName(@NonNull DxdField field) {
    if (field.getType().isObject()) {
      return ClassName.get(
          "com.nilsign.dabacog.demo",
          field.getType().getObjectName());
    }
    if (field.getType().isLong()) {
      return TypeName.LONG;
    }
    if (field.getType().isDouble()) {
      return TypeName.DOUBLE;
    }
    if (field.getType().isInt()) {
      return TypeName.INT;
    }
    if (field.getType().isFloat()) {
      return TypeName.FLOAT;
    }
    if (field.getType().isString()) {
      return TypeName.get(String.class);
    }
    if (field.getType().isBoolean()) {
      return TypeName.BOOLEAN;
    }
    if (field.getType().isDate()) {
      return TypeName.get(Date.class);
    }
    if (field.getType().isBlob()) {
      return TypeName.get(byte[].class);
    }
    throw new JavaEntitiesGeneratorException("Unable to map Dxd field type to Java type.");
  }
}
