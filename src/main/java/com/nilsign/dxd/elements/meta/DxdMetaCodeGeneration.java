package com.nilsign.dxd.elements.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Getter
@Setter
@ToString
@Element(name="codeGeneration")
public class DxdMetaCodeGeneration {

  @Attribute
  String outputLanguage;

  @Attribute
  String outputPath;
}
