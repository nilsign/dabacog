package com.nilsign.dxd.elements.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Getter
@Setter
@ToString
@Element(name="entities")
public class DxdEntities {

  @ElementList(inline=true, entry="class")
  private List<DxdEntityClass> classes;
}
