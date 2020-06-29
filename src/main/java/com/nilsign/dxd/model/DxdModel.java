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

  // Config
  @NonNull
  private final DxdConfig config;

  // Classes
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
  @NonNull private ImmutableMap<String, DxdClass> classNameToClassMap;
  @NonNull private ImmutableMap<DxdField, DxdClass> fieldToClassMap;
  @NonNull private ImmutableMap<DxdClass, ImmutableSet<DxdField>> classToFieldsMap;
  @NonNull private ImmutableMap<String, ImmutableSet<DxdField>> classNameToFieldsMap;
  @NonNull private ImmutableMap<Pair<String, DxdClass>, DxdField> fieldNameToFieldMap;

  // All relations
  @NonNull private ImmutableList<DxdFieldRelation> relations;
  @NonNull private ImmutableList<DxdFieldRelation> manyToManyRelations;
  @NonNull private ImmutableList<DxdFieldRelation> manyToOneRelations;
  @NonNull private ImmutableList<DxdFieldRelation> oneToManyRelations;
  @NonNull private ImmutableList<DxdFieldRelation> oneToOneRelations;

  // Distinct relation
  @NonNull private ImmutableList<DxdFieldRelation> distinctRelations;
  @NonNull private ImmutableList<DxdFieldRelation> distinctManyToManyRelations;
  @NonNull private ImmutableList<DxdFieldRelation> distinctManyToOneRelations;
  @NonNull private ImmutableList<DxdFieldRelation> distinctOneToManyRelations;
  @NonNull private ImmutableList<DxdFieldRelation> distinctOneToOneRelations;

  // Distinct bi-directional relation mappings
  @NonNull private ImmutableList<DxdFieldRelation> distinctBiDirectionalRelations;
  @NonNull private ImmutableList<DxdFieldRelation> distinctBiDirectionalManyToManyRelations;
  @NonNull private ImmutableList<DxdFieldRelation> distinctBiDirectionalManyToOneRelations;
  @NonNull private ImmutableList<DxdFieldRelation> distinctBiDirectionalOneToManyRelations;
  @NonNull private ImmutableList<DxdFieldRelation> distinctBiDirectionalOneToOneRelations;

  // Distinct on-directional relation mappings
  @NonNull private ImmutableList<DxdFieldRelation> oneDirectionalRelations;
  @NonNull private ImmutableList<DxdFieldRelation> oneDirectionalManyToManyRelations;
  @NonNull private ImmutableList<DxdFieldRelation> oneDirectionalManyToOneRelations;
  @NonNull private ImmutableList<DxdFieldRelation> oneDirectionalOneToManyRelations;
  @NonNull private ImmutableList<DxdFieldRelation> oneDirectionalOneToOneRelations;

  private void prepare() {
    classNameToClassMap = DxdModelFactory.createClassNameToClassMap(classes);
    fieldToClassMap = DxdModelFactory.createFieldToClassMap(classes);
    classToFieldsMap = DxdModelFactory.createClassToFieldsMap(classes);
    classNameToFieldsMap = DxdModelFactory.createClassNameToFieldsMap(classes);
    fieldNameToFieldMap = DxdModelFactory.createFieldNameToFieldMap(classes);
    relations = DxdModelFactory.createRelations(classes);
    manyToManyRelations = DxdModelFactory.createManyToManyRelations(relations);
    manyToOneRelations = DxdModelFactory.createManyToOneRelations(relations);
    oneToManyRelations = DxdModelFactory.createOneToManyRelations(relations);
    oneToOneRelations = DxdModelFactory.createOneToOneRelations(relations);
    distinctRelations = DxdModelFactory.createDistinctRelations(relations);
    distinctManyToManyRelations = DxdModelFactory.createDistinctManyToManyRelations(relations);
    distinctManyToOneRelations = DxdModelFactory.createDistinctManyToOneRelations(relations);
    distinctOneToManyRelations = DxdModelFactory.createDistinctOneToManyRelations(relations);
    distinctOneToOneRelations = DxdModelFactory.createDistinctOneToOneRelations(relations);
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
  }
}
