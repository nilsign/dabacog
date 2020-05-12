package com.nilsign.dxd.elements.meta;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name="diagrams")
public class DxdMetaDiagrams {

  @Attribute
  private String outputPath;
}
