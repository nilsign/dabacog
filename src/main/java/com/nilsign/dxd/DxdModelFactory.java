package com.nilsign.dxd;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.nilsign.dxd.model.DxdClass;
import com.nilsign.dxd.model.DxdField;
import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.misc.Pair;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DxdModelFactory {

  public static ImmutableMap<String, DxdClass> createClassNameToClassMap(
      @NonNull List<DxdClass> classes) {
    return Maps.uniqueIndex(classes, DxdClass::getName);
  }

  public static ImmutableMap<DxdField, DxdClass> createFieldToClassMap(
      @NonNull List<DxdClass> classes) {
    Map<DxdField, DxdClass> fieldToClassMap = new HashMap<>();
    classes.forEach(aClass
        -> aClass.getFields().forEach(field
            -> fieldToClassMap.put(field, aClass)));
    return ImmutableMap.copyOf(fieldToClassMap);
  }

  public static ImmutableMap<DxdClass, ImmutableSet<DxdField>> createClassToFieldsMap(
      @NonNull List<DxdClass> classes) {
    Map<DxdClass, ImmutableSet<DxdField>> classToFieldsMap = new HashMap<>();
    classes.forEach(aClass
        -> classToFieldsMap.put(aClass, ImmutableSet.copyOf(aClass.getFields())));
    return ImmutableMap.copyOf(classToFieldsMap);
  }

  public static ImmutableMap<String, ImmutableSet<DxdField>> createClassNameToFieldsMap(
      @NonNull List<DxdClass> classes) {
    Map<String, ImmutableSet<DxdField>> classToFieldsMap = new HashMap<>();
    classes.forEach(aClass
        -> classToFieldsMap.put(aClass.getName(), ImmutableSet.copyOf(aClass.getFields())));
    return ImmutableMap.copyOf(classToFieldsMap);
  }

  public static ImmutableMap<Pair<String, DxdClass>, DxdField> createFieldNameToFieldMap(
      @NonNull List<DxdClass> classes) {
    Map<Pair<String, DxdClass>, DxdField> fieldNameToField = new HashMap<>();
    classes.forEach(aClass
        -> aClass.getFields().forEach(field
            -> fieldNameToField.put(Pair.of(field.getName(), aClass), field)));
    return ImmutableMap.copyOf(fieldNameToField);
  }

  public static ImmutableList<DxdFieldRelation> createRelations(
      @NonNull List<DxdClass> classes) {
    Map<String, DxdClass> classNameToClass = createClassNameToClassMap(classes);
    Map<Pair<String, DxdClass>, DxdField> fieldNameToField = createFieldNameToFieldMap(classes);
    List<DxdFieldRelation> relations = new ArrayList<>();
    classes.forEach(aClass
        -> aClass.getFields().stream().filter(DxdField::hasRelation).forEach(field -> {
          DxdClass referencedClass = classNameToClass.get(field.getType().getObjectName());
          DxdField backReferencingField = fieldNameToField.get(
              Pair.of(aClass.getName(), referencedClass));
          relations.add(DxdFieldRelation.of(
              aClass,
              field,
              referencedClass,
              backReferencingField,
              field.getRelationType()));
        }));
    return ImmutableList.copyOf(relations);
  }

  public static ImmutableList<DxdFieldRelation> createManyToManyRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(DxdFieldRelation::isManyToMany)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createManyToOneRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(DxdFieldRelation::isManyToOne)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createOneToManyRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(DxdFieldRelation::isOneToMany)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createOneToOneRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(DxdFieldRelation::isOneToOne)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createDistinctRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList
        .<DxdFieldRelation>builder()
        .addAll(createDistinctBiDirectionalRelations(relations))
        .addAll(createOneDirectionalRelations(relations))
        .build();
  }

  public static ImmutableList<DxdFieldRelation> createDistinctManyToManyRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList
        .<DxdFieldRelation>builder()
        .addAll(createDistinctBiDirectionalManyToManyRelations(relations))
        .addAll(createOneDirectionalManyToManyRelations(relations))
        .build();
  }

  public static ImmutableList<DxdFieldRelation> createDistinctManyToOneRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList
        .<DxdFieldRelation>builder()
        .addAll(createDistinctBiDirectionalManyToOneRelations(relations))
        .addAll(createOneDirectionalManyToOneRelations(relations))
        .build();
  }

  public static ImmutableList<DxdFieldRelation> createDistinctOneToManyRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList
        .<DxdFieldRelation>builder()
        .addAll(createDistinctBiDirectionalOneToManyRelations(relations))
        .addAll(createOneDirectionalOneToManyRelations(relations))
        .build();
  }

  public static ImmutableList<DxdFieldRelation> createDistinctOneToOneRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList
        .<DxdFieldRelation>builder()
        .addAll(createDistinctBiDirectionalOneToOneRelations(relations))
        .addAll(createOneDirectionalOneToOneRelations(relations))
        .build();
  }

  public static ImmutableList<DxdFieldRelation> createDistinctBiDirectionalRelations(
      @NonNull List<DxdFieldRelation> relations) {
    Set<Pair<DxdClass, DxdClass>> addedDxdClassRelations = new HashSet<>();
    return ImmutableList.copyOf(relations
        .stream()
        .filter(relation -> {
          if (relation.isBiDirectional() && !addedDxdClassRelations.contains(
              Pair.of(relation.getSecondClass(), relation.getFirstClass()))) {
            addedDxdClassRelations.add(
                Pair.of(relation.getFirstClass(), relation.getSecondClass()));
            return true;
          }
          return false;
        })
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createDistinctBiDirectionalManyToManyRelations(
      @NonNull List<DxdFieldRelation> relations) {
    List<DxdFieldRelation> distinctBiDirectionalRelations
        = createDistinctBiDirectionalRelations(relations);
    return ImmutableList.copyOf(distinctBiDirectionalRelations
        .stream()
        .filter(DxdFieldRelation::isManyToMany)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createDistinctBiDirectionalManyToOneRelations(
      @NonNull List<DxdFieldRelation> relations) {
    List<DxdFieldRelation> distinctBiDirectionalRelations
        = createDistinctBiDirectionalRelations(relations);
    return ImmutableList.copyOf(distinctBiDirectionalRelations
        .stream()
        .filter(DxdFieldRelation::isManyToOne)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createDistinctBiDirectionalOneToManyRelations(
      @NonNull List<DxdFieldRelation> relations) {
    List<DxdFieldRelation> distinctBiDirectionalRelations
        = createDistinctBiDirectionalRelations(relations);
    return ImmutableList.copyOf(distinctBiDirectionalRelations
        .stream()
        .filter(DxdFieldRelation::isOneToMany)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createDistinctBiDirectionalOneToOneRelations(
      @NonNull List<DxdFieldRelation> relations) {
    List<DxdFieldRelation> distinctBiDirectionalRelations
        = createDistinctBiDirectionalRelations(relations);
    return ImmutableList.copyOf(distinctBiDirectionalRelations
        .stream()
        .filter(DxdFieldRelation::isOneToOne)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createOneDirectionalRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(DxdFieldRelation::isOneDirectional)
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createOneDirectionalManyToManyRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(relation -> relation.isOneDirectional() && relation.isManyToMany())
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createOneDirectionalManyToOneRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(relation -> relation.isOneDirectional() && relation.isManyToOne())
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createOneDirectionalOneToManyRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(relation -> relation.isOneDirectional() && relation.isOneToMany())
        .collect(Collectors.toList()));
  }

  public static ImmutableList<DxdFieldRelation> createOneDirectionalOneToOneRelations(
      @NonNull List<DxdFieldRelation> relations) {
    return ImmutableList.copyOf(relations
        .stream()
        .filter(relation -> relation.isOneDirectional() && relation.isOneToOne())
        .collect(Collectors.toList()));
  }
}
