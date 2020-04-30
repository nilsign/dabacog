package com.nilsign.dxd.elements.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Getter
@Setter
@ToString
@Element(name="class")
public class DxdEntityClass {

  @Attribute
  private String name;

  @ElementList(inline=true, entry="field")
  private List<DxdEntityClassField> fields;
}
