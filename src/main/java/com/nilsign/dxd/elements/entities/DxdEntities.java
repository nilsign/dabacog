package com.nilsign.dxd.elements.entities;

import com.google.common.collect.Sets;
import com.nilsign.misc.Pair;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Element(name="entities")
public class DxdEntities {

  // Class mappings
  private final Map<String, DxdEntityClass> classNameToClassMap = new HashMap();
  private final Map<DxdEntityClassField, DxdEntityClass> fieldToClassMap = new HashMap<>();
  private final Map<DxdEntityClass, Set<DxdEntityClassField>> classToFieldsMap = new HashMap<>();
  private final Map<String, Set<DxdEntityClassField>> classNameToFieldsMap = new HashMap<>();

  // Class relation mappings
  private final Map<DxdEntityClass, Set<DxdEntityClass>> manyToManyClassRelationsMap
      = new HashMap<>();
  private final Map<DxdEntityClass, Set<DxdEntityClass>> manyToOneClassRelationsMap
      = new HashMap<>();
  private final Map<DxdEntityClass, Set<DxdEntityClass>> oneToManyClassRelationsMap
      = new HashMap<>();
  private final Map<DxdEntityClass, Set<DxdEntityClass>> oneToOneClassRelationsMap
      = new HashMap<>();

  // Distinct class relation lists (when A -> B, then B -> A is inherent and therefor excluded).
  private final List<Pair<DxdEntityClass, DxdEntityClass>> distinctManyToManyClassRelationsList
      = new ArrayList<>();
  private final List<Pair<DxdEntityClass, DxdEntityClass>> distinctManyToOneClassRelationsList
      = new ArrayList<>();
  private final List<Pair<DxdEntityClass, DxdEntityClass>> distinctOneToManyClassRelationsList
      = new ArrayList<>();
  private final List<Pair<DxdEntityClass, DxdEntityClass>> distinctOneToOneClassRelationsList
      = new ArrayList<>();

  @ElementList(inline=true, entry="class")
  private List<DxdEntityClass> dxdClasses;

  public void prepareModels() {
    createClassMappings();
    createClassRelationMappings();
    createDistinctClassRelationLists();
  }

  private void createClassMappings() {
    dxdClasses.forEach(dxdClass -> {
      classNameToClassMap.put(dxdClass.getName(), dxdClass);
      classToFieldsMap.put(
          dxdClass,
          Sets.newHashSet(dxdClass.getFields()));
      classNameToFieldsMap.put(
          dxdClass.getName(),
          Sets.newHashSet(dxdClass.getFields()));
      dxdClass.getFields().forEach(dxdField
          -> fieldToClassMap.put(dxdField, dxdClass));
    });
  }

  public void createClassRelationMappings() {
    dxdClasses.forEach(dxdClass
        -> dxdClass.getRelationFields().forEach(dxdField -> {
          DxdEntityClass referredDxdClass = classNameToClassMap.get(dxdField.getRefersTo());
          boolean hasBackReference = false;
          for (DxdEntityClassField referredDxdField : referredDxdClass.getRelationFields()) {
            if (dxdClass.getName().equalsIgnoreCase(referredDxdField.getRefersTo())) {
              // Referenced class has a back referencing field.
              if (dxdField.isToManyRelation() && referredDxdField.isToManyRelation()) {
                hasBackReference = true;
                addRelationToMap(manyToManyClassRelationsMap, dxdClass, referredDxdClass);
              } else if (dxdField.isToManyRelation() && referredDxdField.isToOneRelation()) {
                hasBackReference = true;
                addRelationToMap(manyToOneClassRelationsMap, dxdClass, referredDxdClass);
              } else if (dxdField.isToOneRelation() && referredDxdField.isToManyRelation()) {
                hasBackReference = true;
                addRelationToMap(oneToManyClassRelationsMap, dxdClass, referredDxdClass);
              } else if (dxdField.isToOneRelation() && referredDxdField.isToOneRelation()) {
                hasBackReference = true;
                addRelationToMap(oneToOneClassRelationsMap, dxdClass, referredDxdClass);
              }
            }
          }
          if (!hasBackReference) {
            if (dxdField.isToManyRelation()) {
              // If a multiplicity many field has no back reference field within the referenced class
              // a many-to-many relation is assumed.
              addRelationToMap(manyToManyClassRelationsMap, dxdClass, referredDxdClass);
            } else if (dxdField.isToOneRelation()) {
              // If a multiplicity many field has no back reference field within the referenced class
              // a many-to-many relation is assumed.
              addRelationToMap(oneToManyClassRelationsMap, dxdClass, referredDxdClass);
              addRelationToMap(manyToOneClassRelationsMap, referredDxdClass, dxdClass);
            }
          }
        }));
  }

  private void createDistinctClassRelationLists() {
    Set<Pair<DxdEntityClass, DxdEntityClass>> addedDxdClassRelations = new HashSet<>();
    manyToManyClassRelationsMap.entrySet()
        .forEach((Map.Entry<DxdEntityClass, Set<DxdEntityClass>> entry)
            -> entry.getValue().forEach((DxdEntityClass referencedClass) -> {
                  Pair<DxdEntityClass, DxdEntityClass> relationToAdd
                      = Pair.of(entry.getKey(), referencedClass);
                  if (!addedDxdClassRelations.contains(relationToAdd)
                      && !addedDxdClassRelations.contains(relationToAdd.invert())) {
                    distinctManyToManyClassRelationsList.add(relationToAdd);
                    addedDxdClassRelations.add(relationToAdd.invert());
                  }
                }));
    addedDxdClassRelations.clear();
    manyToOneClassRelationsMap.entrySet()
        .forEach((Map.Entry<DxdEntityClass, Set<DxdEntityClass>> entry)
            -> entry.getValue().forEach((DxdEntityClass referencedClass) -> {
                Pair<DxdEntityClass, DxdEntityClass> relationToAdd
                    = Pair.of(entry.getKey(), referencedClass);
                if (!addedDxdClassRelations.contains(relationToAdd)
                    && !addedDxdClassRelations.contains(relationToAdd.invert())) {
                  distinctManyToOneClassRelationsList.add(relationToAdd);
                  addedDxdClassRelations.add(relationToAdd.invert());
                }
              }));
    addedDxdClassRelations.clear();
    oneToManyClassRelationsMap.entrySet()
        .forEach((Map.Entry<DxdEntityClass, Set<DxdEntityClass>> entry)
            -> entry.getValue().forEach((DxdEntityClass referencedClass) -> {
              Pair<DxdEntityClass, DxdEntityClass> relationToAdd
                  = Pair.of(entry.getKey(), referencedClass);
              if (!addedDxdClassRelations.contains(relationToAdd)
                  && !addedDxdClassRelations.contains(relationToAdd.invert())) {
                distinctOneToManyClassRelationsList.add(relationToAdd);
                addedDxdClassRelations.add(relationToAdd.invert());
              }
            }));
    addedDxdClassRelations.clear();
    oneToOneClassRelationsMap.entrySet()
        .forEach((Map.Entry<DxdEntityClass, Set<DxdEntityClass>> entry)
            -> entry.getValue().forEach((DxdEntityClass referencedClass) -> {
              Pair<DxdEntityClass, DxdEntityClass> relationToAdd
                  = Pair.of(entry.getKey(), referencedClass);
              if (!addedDxdClassRelations.contains(relationToAdd)
                  && !addedDxdClassRelations.contains(relationToAdd.invert())) {
                distinctOneToOneClassRelationsList.add(relationToAdd);
                addedDxdClassRelations.add(relationToAdd.invert());
              }
            }));
  }

  private void addRelationToMap(
      Map<DxdEntityClass, Set<DxdEntityClass>> relationMap,
      DxdEntityClass dxdClass,
      DxdEntityClass referredDxdClass) {
    if (relationMap.get(dxdClass) == null) {
      relationMap.put(dxdClass, new HashSet<>());
    }
    relationMap.get(dxdClass).add(referredDxdClass);
  }
}
