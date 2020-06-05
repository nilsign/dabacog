package com.nilsign.dxd.xml.entities;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.HashSet;
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

  @EqualsAndHashCode.Exclude
  private final Set<DxdEntityRelation> relations = new HashSet<>();

  public Set<DxdEntityField> getRelationFields() {
    return fields
        .stream()
        .filter(DxdEntityField::isRelation)
        .collect(Collectors.toSet());
  }

  public DxdEntityField findReferenceFieldTo(@NonNull DxdEntityClass aClass) {
    return getRelationFields().size() == 0
        ? null
        : getRelationFields()
          .stream()
          .filter((DxdEntityField field)
              -> aClass.getName().equalsIgnoreCase(field.getFieldType().getTypeName()))
          .findFirst()
          .orElse(null);
  }

  public void addRelation(@NonNull DxdEntityRelation relation) {
   relations. add(relation);
  }
}
