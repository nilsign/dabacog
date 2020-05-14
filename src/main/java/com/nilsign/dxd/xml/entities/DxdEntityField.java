package com.nilsign.dxd.xml.entities;

import com.nilsign.dxd.noxml.DxdEntityRelation;
import com.nilsign.dxd.xmlvaluetypes.DxdFieldMultiplicity;
import com.nilsign.dxd.xmlvaluetypes.DxdFieldType;
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
  private DxdFieldMultiplicity multiplicity;

  @Attribute(required=false)
  private boolean hidden;

  @Attribute(required=false)
  private boolean indexed;

  @Attribute(required=false)
  private String name;

  @Attribute(required=false)
  private DxdFieldType type;

  @Attribute(required=false)
  private boolean unique;

  @Attribute(required=false)
  private boolean nullable;

  // TODO(nilsheumer): Add support for full test search.
  // https://hackernoon.com/how-useful-is-postgresql-full-text-search-u39242fi
  @Attribute(required=false)
  private boolean fts = false;

  @Attribute(name="default", required=false)
  private String defaultValue;

  @EqualsAndHashCode.Exclude
  private DxdEntityClass parentClass;

  @EqualsAndHashCode.Exclude
  private DxdEntityRelation relation;

  public boolean isRelation() {
    return isToManyRelation() || isToOneRelation();
  }

  public boolean isToManyRelation() {
    return multiplicity == DxdFieldMultiplicity.MANY;
  }

  public boolean isToOneRelation() {
    return multiplicity == DxdFieldMultiplicity.ONE;
  }
}
