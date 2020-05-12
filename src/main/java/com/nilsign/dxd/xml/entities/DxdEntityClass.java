package com.nilsign.dxd.xml.entities;

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
  private List<DxdEntityField> fields;

  public Set<DxdEntityField> getRelationFields() {
    return fields
        .stream()
        .filter(DxdEntityField::isRelation)
        .collect(Collectors.toSet());
  }

  public Set<DxdEntityField> getToManyRelationFields() {
   return fields
       .stream()
       .filter(DxdEntityField::isToManyRelation)
       .collect(Collectors.toSet());
  }

  public Set<DxdEntityField> getToOneRelationFields() {
    return fields
        .stream()
        .filter(DxdEntityField::isToOneRelation)
        .collect(Collectors.toSet());
  }


}
