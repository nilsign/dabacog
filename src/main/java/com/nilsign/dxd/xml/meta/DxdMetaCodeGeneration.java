package com.nilsign.dxd.xml.meta;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name="codeGeneration")
public class DxdMetaCodeGeneration {

  @Attribute
  private String outputLanguage;

  @Attribute
  private String outputPath;
}
