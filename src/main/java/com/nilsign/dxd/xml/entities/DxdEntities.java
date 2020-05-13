package com.nilsign.dxd.xml.entities;

import com.nilsign.dxd.noxml.DxdEntityRelation;
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
  private final List<DxdEntityRelation> relations = new ArrayList<>();
  private final List<DxdEntityRelation> manyToManyRelations = new ArrayList<>();
  private final List<DxdEntityRelation> manyToOneRelations = new ArrayList<>();
  private final List<DxdEntityRelation> oneToManyRelations = new ArrayList<>();
  private final List<DxdEntityRelation> oneToOneRelations = new ArrayList<>();

  @ElementList(inline=true, entry="class")
  private List<DxdEntityClass> dxdClasses;

  public void prepareModels() throws DxdModelException {
    try {
      createCommonMappings();
      createDistinctRelations();
    } catch (Exception e) {
      throw new DxdModelException("The Dxd model preparation failed.", e);
    }
  }

  private void createCommonMappings() {
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

  public void createDistinctRelations() {
    Set<Pair<DxdEntityClass, DxdEntityClass>> addedDxdClassRelations = new HashSet<>();
    dxdClasses.forEach(dxdClass
        -> dxdClass.getRelationFields().forEach(dxdField -> {
          DxdEntityClass referencedDxdClass = classNameToClassMap.get(dxdField.getRefersTo());
          DxdEntityRelation relation = DxdEntityRelation.of(dxdClass, dxdField, referencedDxdClass);
          if (!addedDxdClassRelations.contains(
              Pair.of(relation.getReferencedClass(), relation.getReferencingClass()))) {
            relations.add(relation);
            switch (relation.getType()) {
              case MANY_TO_MANY: manyToManyRelations.add(relation); break;
              case MANY_TO_ONE: manyToOneRelations.add(relation); break;
              case ONE_TO_MANY: oneToManyRelations.add(relation); break;
              case ONE_TO_ONE: oneToOneRelations.add(relation); break;
            }
            dxdClass.addRelation(relation);
            referencedDxdClass.addRelation(relation);
            addedDxdClassRelations.add(
                Pair.of(relation.getReferencingClass(), relation.getReferencedClass()));
          }
        }));
   }
}
