package com.nilsign.dxd.elements.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Getter
@Setter
@ToString
@Element(name="field")
public class DxdEntityClassField {

  @Attribute(required=false)
  private String refersTo;

  @Attribute(required=false)
  private String multiplicity;

  @Attribute(required=false)
  private String name;

  @Attribute(required=false)
  private String type;
}
