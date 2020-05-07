package com.nilsign.dxd.elements.meta;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name="database")
public class DxdMetaDatabase {

  @Attribute
  private String system;

  @Attribute
  private String location;
}
