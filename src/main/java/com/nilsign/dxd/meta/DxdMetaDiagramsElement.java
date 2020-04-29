package com.nilsign.dxd.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Getter
@Setter
@ToString
@Element(name="diagrams")
public class DxdMetaDiagramsElement {

  @Attribute
  private String outputPath;

  @Attribute
  private String outputFormat;
}
