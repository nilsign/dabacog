package com.nilsign.dxd.xml.entities;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xmlvaluetypes.DxdAttributeMultiplicity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name="field")
public class DxdEntityField {

  public static final String ATTRIBUTE_NAME_MULTIPLICITY = "multiplicity";

  @Attribute(required=false)
  private String refersTo;

  @Attribute(required=false)
  private DxdAttributeMultiplicity multiplicity;

  @Attribute(required=false)
  private String name;

  @Attribute(required=false)
  private String type;

  @EqualsAndHashCode.Exclude
  private DxdEntityClass parentClass;

  @EqualsAndHashCode.Exclude
  private DxdEntityRelation relation;

  public boolean isRelation() {
    return isToManyRelation() || isToOneRelation();
  }

  public boolean isToManyRelation() {
    return multiplicity == DxdAttributeMultiplicity.MANY;
  }

  public boolean isToOneRelation() {
    return multiplicity == DxdAttributeMultiplicity.ONE;
  }
}
