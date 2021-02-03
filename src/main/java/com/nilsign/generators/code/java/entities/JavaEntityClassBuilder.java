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
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.NonNull;

import javax.lang.model.element.Modifier;
import java.util.Date;
import java.util.List;

public final class JavaEntityClassBuilder {

  public static String buildEntityClass(@NonNull DxdModel model, @NonNull DxdClass aClass) {
    TypeSpec javaClass = TypeSpec.classBuilder(Java.normalizeClassName(aClass.getName()))
        .addFields(buildFieldSpecs(model, aClass.getFields()))
        .addMethods(buildMethodSpecs(model, aClass.getFields()))
        .build();
    return JavaFile
        .builder(model.getConfig().getCodePackageName(), javaClass)
        .build()
        .toString();
  }

  private static List<FieldSpec> buildFieldSpecs(
      @NonNull DxdModel model,
      @NonNull List<DxdField> fields) {
    List<FieldSpec> methodSpecs = Lists.newArrayList(
        buildPrimaryKeyFieldSpec());
    fields.forEach(field -> methodSpecs.add(
        buildFieldSpec(model, field)));
    return methodSpecs;

  }

  private static FieldSpec buildPrimaryKeyFieldSpec() {
    try {
      return FieldSpec
          .builder(
              TypeName.LONG,
              Java.normalizeFieldName(Sql.SQL_PRIMARY_KEY_NAME),
              Modifier.PRIVATE)
          .build();
    }  catch (Exception e) {
        throw new RuntimeException("Failed to generate primary key field.", e);
      }
    }

  private static FieldSpec buildFieldSpec(@NonNull DxdModel model, @NonNull DxdField field) {
    try {
      return FieldSpec
          .builder(
              getJavaTypeName(model, field),
              Java.normalizeFieldName(field.getName()),
              Modifier.PRIVATE)
          .build();
      }  catch (Exception e) {
      throw new RuntimeException(
          String.format(
              "Failed to generate field for '%s'.",
              field.getName()),
          e);
    }
  }

  private static List<MethodSpec> buildMethodSpecs(
      @NonNull DxdModel model,
      @NonNull List<DxdField> fields) {
    List<MethodSpec> methodSpecs = Lists.newArrayList(
        buildPrimaryKeyGetterMethodSpec(),
        buildPrimaryKeySetterMethodSpec());
    fields.forEach(field -> methodSpecs.addAll(List.of(
        buildGetterMethodSpec(model, field),
        buildSetterMethodSpec(model, field))));
    return methodSpecs;
  }

  private static MethodSpec buildPrimaryKeyGetterMethodSpec() {
    try {
      return MethodSpec
          .methodBuilder(String.format("get%s", Java.startUpperCased(Sql.SQL_PRIMARY_KEY_NAME)))
          .addModifiers(Modifier.PUBLIC)
          .addStatement(
              String.format("return %s", Java.normalizeFieldName(Sql.SQL_PRIMARY_KEY_NAME)))
          .returns(TypeName.LONG)
          .build();
    }  catch (Exception e) {
      throw new RuntimeException("Failed to generate primary key getter function.", e);
    }
  }

  private static MethodSpec buildPrimaryKeySetterMethodSpec() {
    try {
      String fieldName = Java.normalizeFieldName(Sql.SQL_PRIMARY_KEY_NAME);
      return MethodSpec.methodBuilder(
              String.format("set%s", Java.startUpperCased(Sql.SQL_PRIMARY_KEY_NAME)))
          .addModifiers(Modifier.PUBLIC)
          .addParameter(TypeName.LONG, fieldName)
          .addStatement(String.format("this.%s = %s", fieldName, fieldName))
          .build();
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate primary key setter function.", e);
    }
  }

  private static MethodSpec buildGetterMethodSpec(
      @NonNull DxdModel model,
      @NonNull DxdField field) {
    try {
      return MethodSpec.methodBuilder(String.format("get%s", field.getName()))
          .addModifiers(Modifier.PUBLIC)
          .addStatement(String.format("return %s", Java.normalizeFieldName(field.getName())))
          .returns(getJavaTypeName(model, field))
          .build();
      } catch (Exception e) {
      throw new RuntimeException(
          String.format(
              "Failed to generate '%s' getter function.",
              field.getName()),
          e);
    }
  }

  private static MethodSpec buildSetterMethodSpec(
      @NonNull DxdModel model,
      @NonNull DxdField field) {
    try {
      String fieldName = Java.normalizeFieldName(field.getName());
      return MethodSpec.methodBuilder(String.format("set%s", field.getName()))
          .addModifiers(Modifier.PUBLIC)
          .addParameter(getJavaTypeName(model, field), fieldName)
          .addStatement(String.format("this.%s = %s", field.getName(), field.getName()))
          .build();
    } catch (Exception e) {
      throw new RuntimeException(
          String.format(
              "Failed to generate '%s' setter function.",
              field.getName()),
          e);
      }
  }

  private static TypeName getJavaTypeName(@NonNull DxdModel model, @NonNull DxdField field) {
    if (field.getType().isObject()) {
      return field.getRelationType().isManyToMany() || field.getRelationType().isManyToOne()
          ? ParameterizedTypeName.get(
              ClassName.get(List.class),
              ClassName.get(
                  model.getConfig().getCodePackageName(),
                  Java.normalizeClassName(field.getName())))
          : ClassName.get(
              model.getConfig().getCodePackageName(),
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
    throw new RuntimeException("Unable to map Dxd field type to Java type.");
  }
}
