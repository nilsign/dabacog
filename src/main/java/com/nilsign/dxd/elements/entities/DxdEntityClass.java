package com.nilsign.dxd.elements.entities;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Element(name="class")
public class DxdEntityClass {

  @Attribute
  private String name;

  @ElementList(inline=true, entry="field")
  private List<DxdEntityClassField> fields;

  public Set<DxdEntityClassField> getRelationFields() {
    return fields
        .stream()
        .filter(DxdEntityClassField::isRelation)
        .collect(Collectors.toSet());
  }

  public Set<DxdEntityClassField> getToManyRelationFields() {
   return fields
       .stream()
       .filter(DxdEntityClassField::isToManyRelation)
       .collect(Collectors.toSet());
  }

  public Set<DxdEntityClassField> getToOneRelationFields() {
    return fields
        .stream()
        .filter(DxdEntityClassField::isToOneRelation)
        .collect(Collectors.toSet());
  }


}
