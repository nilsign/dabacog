package com.nilsign.dxd.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.nilsign.dxd.DxdModelFactory;
import com.nilsign.misc.Pair;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public final class DxdModel {

  @NonNull
  private final String name;

  @NonNull
  private final DxdConfig config;

  @NonNull
  private final ImmutableList<DxdClass> classes;

  public static DxdModel of(String name, DxdConfig config, List<DxdClass> classes) {
    return new DxdModel(name, config, classes);
  }

  private DxdModel(String name, DxdConfig config, List<DxdClass> classes) {
    this.name = name;
    this.config = config;
    this.classes = ImmutableList.copyOf(classes);
    prepare();
  }

  // Common mappings
  @NonNull
  private ImmutableMap<String, DxdClass> classNameToClassMap;

  @NonNull
  private ImmutableMap<DxdField, DxdClass> fieldToClassMap;

  @NonNull
  private ImmutableMap<DxdClass, ImmutableSet<DxdField>> classToFieldsMap;

  @NonNull
  private ImmutableMap<String, ImmutableSet<DxdField>> classNameToFieldsMap;

  @NonNull
  private ImmutableMap<Pair<String, DxdClass>, DxdField> fieldNameToFieldMap;

  // All relations
  @NonNull
  private ImmutableList<DxdFieldRelation> relations;

  @NonNull
  private ImmutableList<DxdFieldRelation> manyToManyRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> manyToOneRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> oneToManyRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> oneToOneRelations;

  // Distinct relations
  @NonNull
  private ImmutableList<DxdFieldRelation> distinctRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> distinctManyToManyRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> distinctManyToOneRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> distinctOneToManyRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> distinctOneToOneRelations;

  // Distinct bi-directional relations
  @NonNull
  private ImmutableList<DxdFieldRelation> distinctBiDirectionalRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> distinctBiDirectionalManyToManyRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> distinctBiDirectionalManyToOneRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> distinctBiDirectionalOneToManyRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> distinctBiDirectionalOneToOneRelations;

  // Distinct on-directional relation
  @NonNull
  private ImmutableList<DxdFieldRelation> oneDirectionalRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> oneDirectionalManyToManyRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> oneDirectionalManyToOneRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> oneDirectionalOneToManyRelations;

  @NonNull
  private ImmutableList<DxdFieldRelation> oneDirectionalOneToOneRelations;

  private void prepare() {
    createCommonMappings();
    createRelations();
    createDistinctRelations();
    createDistinctBiDirectionalRelations();
    createOneDirectionalRelations();
  }

  private void createCommonMappings() {
    try {
      classNameToClassMap
          = DxdModelFactory.createClassNameToClassMap(classes);
      fieldToClassMap
          = DxdModelFactory.createFieldToClassMap(classes);
      classToFieldsMap
          = DxdModelFactory.createClassToFieldsMap(classes);
      classNameToFieldsMap
          = DxdModelFactory.createClassNameToFieldsMap(classes);
      fieldNameToFieldMap
          = DxdModelFactory.createFieldNameToFieldMap(classes);
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to create the common Dxd model mappings from the Dxd classes.", e);
    }
  }

  private void createRelations() {
    try {
      relations
          = DxdModelFactory.createRelations(classes);
      manyToManyRelations
          = DxdModelFactory.createManyToManyRelations(relations);
      manyToOneRelations
          = DxdModelFactory.createManyToOneRelations(relations);
      oneToManyRelations
          = DxdModelFactory.createOneToManyRelations(relations);
      oneToOneRelations
          = DxdModelFactory.createOneToOneRelations(relations);
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to create all Dxd relations from the Dxd classes.", e);
    }
  }

  private void createDistinctRelations() {
    try {
      distinctRelations
          = DxdModelFactory.createDistinctRelations(relations);
      distinctManyToManyRelations
          = DxdModelFactory.createDistinctManyToManyRelations(relations);
      distinctManyToOneRelations
          = DxdModelFactory.createDistinctManyToOneRelations(relations);
      distinctOneToManyRelations
          = DxdModelFactory.createDistinctOneToManyRelations(relations);
      distinctOneToOneRelations
          = DxdModelFactory.createDistinctOneToOneRelations(relations);
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to filter all distinct Dxd relations from the Dxd relations.", e);
    }
  }

  private void createDistinctBiDirectionalRelations() {
    try {
      distinctBiDirectionalRelations
          = DxdModelFactory.createDistinctBiDirectionalRelations(relations);
      distinctBiDirectionalManyToManyRelations
          = DxdModelFactory.createDistinctBiDirectionalManyToManyRelations(relations);
      distinctBiDirectionalManyToOneRelations
          = DxdModelFactory.createDistinctBiDirectionalManyToOneRelations(relations);
      distinctBiDirectionalOneToManyRelations
          = DxdModelFactory.createDistinctBiDirectionalOneToManyRelations(relations);
      distinctBiDirectionalOneToOneRelations
          = DxdModelFactory.createDistinctBiDirectionalOneToOneRelations(relations);
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to filter all distinct bi-directional Dxd relations from the Dxd relations.", e);
    }
  }

  private void createOneDirectionalRelations() {
    try {
      oneDirectionalRelations
          = DxdModelFactory.createOneDirectionalRelations(relations);
      oneDirectionalManyToManyRelations
          = DxdModelFactory.createOneDirectionalManyToManyRelations(relations);
      oneDirectionalManyToOneRelations
          = DxdModelFactory.createOneDirectionalManyToOneRelations(relations);
      oneDirectionalOneToManyRelations
          = DxdModelFactory.createOneDirectionalOneToManyRelations(relations);
      oneDirectionalOneToOneRelations
          = DxdModelFactory.createOneDirectionalOneToOneRelations(relations);
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to filter all one-directional Dxd relations from the Dxd relations.", e);
    }
  }
gi
  @Override
  public String toString() {
    StringBuffer output = new StringBuffer()
        .append(String.format("\t%s - Name: %s\n", DxdModel.class.getSimpleName(), name))
        .append(config.toString("\t\t"));
    classes.forEach(aClass
        -> output.append(aClass.toString("\t\t")));
    return output
        .append(convertRelationsToString(
            "Relations",
            relations))
        .append(convertRelationsToString(
            "n..n Relations",
            manyToManyRelations))
        .append(convertRelationsToString(
            "n..1 Relations",
            manyToOneRelations))
        .append(convertRelationsToString(
            "1..n Relations",
            oneToManyRelations))
        .append(convertRelationsToString(
            "1..1 Relations",
            oneToOneRelations))
        .append(convertRelationsToString(
            "Distinct Relations",
            distinctRelations))
        .append(convertRelationsToString(
            "n..n Distinct Relations",
            distinctManyToManyRelations))
        .append(convertRelationsToString(
            "n..1 Distinct Relations",
            distinctManyToOneRelations))
        .append(convertRelationsToString(
            "1..n Distinct Relations",
            distinctOneToManyRelations))
        .append(convertRelationsToString(
            "1..1 Distinct Relations",
            distinctOneToOneRelations))
        .append(convertRelationsToString(
            "Distinct Bi-Directional Relations",
            distinctBiDirectionalRelations))
        .append(convertRelationsToString(
            "n..n Distinct Bi-Directional Relations",
            distinctBiDirectionalManyToManyRelations))
        .append(convertRelationsToString(
            "n..1 Distinct Bi-Directional Relations",
            distinctBiDirectionalManyToOneRelations))
        .append(convertRelationsToString(
            "1..n Distinct Bi-Directional Relations",
            distinctBiDirectionalOneToManyRelations))
        .append(convertRelationsToString(
            "1..1 Distinct Bi-Directional Relations",
            distinctBiDirectionalOneToOneRelations))
        .append(convertRelationsToString(
            "One-Directional Relations",
            oneDirectionalRelations))
        .append(convertRelationsToString(
            "n..n One-Directional Relations",
            oneDirectionalManyToManyRelations))
        .append(convertRelationsToString(
            "n..1 One-Directional Relations",
            oneDirectionalManyToOneRelations))
        .append(convertRelationsToString(
            "1..n One-Directional Relations",
            oneDirectionalOneToManyRelations))
        .append(convertRelationsToString(
            "1..1 One-Directional Relations",
            oneDirectionalOneToOneRelations))
        .toString();
  }

  private String convertRelationsToString(
      @NonNull String name,
      @NonNull List<DxdFieldRelation> relations) {
    StringBuffer output = new StringBuffer()
        .append(String.format("\t\t%s\n", name));
    relations.forEach(relation
        -> output.append(String.format("\t\t\t%s", relation.toString())));
    return output.toString();
  }
}
