package com.nilsign.generators.code.java.entities;

import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdModel;
import com.nilsign.generators.code.java.Java;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.NonNull;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class JavaEntityClassBuilder {

  public static String buildEntityClass(@NonNull DxdModel model, @NonNull DxdClass aClass) {
    TypeSpec javaClass = TypeSpec.classBuilder(Java.normalizeClassName(aClass.getName()))
        .addFields(buildFieldSpecs(aClass.getFields()))
        .build();
    return JavaFile
        .builder(model.getConfig().getCodePackageName(), javaClass)
        .build()
        .toString();
  }

  private static List<FieldSpec> buildFieldSpecs(@NonNull List<DxdField> fields) {
    List<FieldSpec> fieldSpecs = new ArrayList<>();
    fields.forEach(field -> fieldSpecs.add(buildFieldSpec(field)));
    return fieldSpecs;
  }

  private static FieldSpec buildFieldSpec(@NonNull DxdField field) {
    return
        FieldSpec.builder(field.getType().isObject()
            ? ClassName.get("com.nilsign.dabacog.demo", field.getType().getObjectName())
            : getJavaTypeName(field), field.getName(), Modifier.PRIVATE)
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
