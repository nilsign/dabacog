package com.nilsign.dxd.elements.entities;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Data
@Element(name="class")
public class DxdEntityClass {

  @Attribute
  private String name;

  @ElementList(inline=true, entry="field")
  private List<DxdEntityClassField> fields;
}
