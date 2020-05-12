package com.nilsign.dxd.xml.entities;

import com.nilsign.dxd.xml.DxdModelException;
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
  private final Map<DxdEntityField, DxdEntityClass> fieldToClassMap = new HashMap<>();
  private final Map<DxdEntityClass, Set<DxdEntityField>> classToFieldsMap = new HashMap<>();
  private final Map<String, Set<DxdEntityField>> classNameToFieldsMap = new HashMap<>();

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

  public void prepareModels() throws DxdModelException {
    try {
      createClassMappings();
      createClassRelationMappings();
      createDistinctClassRelationLists();
    } catch (Exception e) {
      throw new DxdModelException("The Dxd model preparation failed.", e);
    }
  }

  private void createClassMappings() {
    dxdClasses.forEach(dxdClass -> {
      classNameToClassMap.put(dxdClass.getName(), dxdClass);
      classToFieldsMap.put(
          dxdClass,
          new HashSet<>(dxdClass.getFields()));
      classNameToFieldsMap.put(
          dxdClass.getName(),
          new HashSet<>(dxdClass.getFields()));
      dxdClass.getFields().forEach(dxdField
          -> fieldToClassMap.put(dxdField, dxdClass));
    });
  }

  public void createClassRelationMappings() {
    dxdClasses.forEach(dxdClass
        -> dxdClass.getRelationFields().forEach(dxdField -> {
          DxdEntityClass referredDxdClass = classNameToClassMap.get(dxdField.getRefersTo());
          boolean hasBackReference = false;
          for (DxdEntityField referredDxdField : referredDxdClass.getRelationFields()) {
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

  private void addRelationToMap(
      Map<DxdEntityClass, Set<DxdEntityClass>> relationMap,
      DxdEntityClass dxdClass,
      DxdEntityClass referredDxdClass) {
    if (relationMap.get(dxdClass) == null) {
      relationMap.put(dxdClass, new HashSet<>());
    }
    relationMap.get(dxdClass).add(referredDxdClass);
  }

  private void createDistinctClassRelationLists() {
    distinctManyToManyClassRelationsList.addAll(
        createDistinctClassRelationList(manyToManyClassRelationsMap));
    distinctManyToOneClassRelationsList.addAll(
        createDistinctClassRelationList(manyToOneClassRelationsMap));
    distinctOneToManyClassRelationsList.addAll(
        createDistinctClassRelationList(oneToManyClassRelationsMap));
    distinctOneToOneClassRelationsList.addAll(
        createDistinctClassRelationList(oneToOneClassRelationsMap));
  }

  private List<Pair<DxdEntityClass, DxdEntityClass>> createDistinctClassRelationList(
      Map<DxdEntityClass, Set<DxdEntityClass>> relationsMap) {
    List<Pair<DxdEntityClass, DxdEntityClass>> distinctRelations = new ArrayList<>();
    Set<Pair<DxdEntityClass, DxdEntityClass>> addedDxdClassRelations = new HashSet<>();
    relationsMap.entrySet()
        .forEach((Map.Entry<DxdEntityClass, Set<DxdEntityClass>> entry)
            -> entry.getValue().forEach((DxdEntityClass referencedClass) -> {
              Pair<DxdEntityClass, DxdEntityClass> relationToAdd
                  = Pair.of(entry.getKey(), referencedClass);
              if (!addedDxdClassRelations.contains(relationToAdd)
                  && !addedDxdClassRelations.contains(relationToAdd.invert())) {
                distinctRelations.add(relationToAdd);
                addedDxdClassRelations.add(relationToAdd.invert());
              }
            }));
    return distinctRelations;
  }
}
