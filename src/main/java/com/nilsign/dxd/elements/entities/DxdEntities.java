package com.nilsign.dxd.elements.entities;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Data
@Element(name="entities")
public class DxdEntities {

  @ElementList(inline=true, entry="class")
  private List<DxdEntityClass> classes;
}
