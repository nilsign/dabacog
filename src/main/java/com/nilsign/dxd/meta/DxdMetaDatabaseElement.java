package com.nilsign.dxd.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Getter
@Setter
@ToString
@Element(name="database")
class DxdMetaDatabaseElement {

  @Attribute
  private String system;

  @Attribute
  private String location;
}