package com.nilsign.dxd.elements.entities;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name="field")
public class DxdEntityClassField {

  public static final String ATTRIBUTE_NAME_MULTIPLICITY = "multiplicity";

  @Attribute(required=false)
  private String refersTo;

  @Attribute(required=false)
  private Multiplicity multiplicity;

  @Attribute(required=false)
  private String name;

  @Attribute(required=false)
  private String type;

  public boolean isRelation() {
    return isToManyRelation() || isToOneRelation();
  }

  public boolean isToManyRelation() {
    return multiplicity == Multiplicity.MANY;
  }

  public boolean isToOneRelation() {
    return multiplicity == Multiplicity.ONE;
  }
}
