package com.nilsign.dxd.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Element;

@Getter
@Setter
@ToString
@Element(name="meta")
public class DxdMetaElement {

  @Element(name="diagrams")
  private DxdMetaDiagramsElement dxdMetaDiagrams;

  @Element(name="database")
  private DxdMetaDatabaseElement dxdMetaDatabase;

  @Element(name="codeGeneration")
  private DxdMetaCodeGenerationElement dxdMetaCodeGeneration;
}



