package com.nilsign.dxd.elements.entities;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
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
