package com.nilsign.dxd.elements.meta;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name="codeGeneration")
public class DxdMetaCodeGeneration {

  @Attribute
  String outputLanguage;

  @Attribute
  String outputPath;
}
