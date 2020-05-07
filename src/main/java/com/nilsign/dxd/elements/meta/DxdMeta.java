package com.nilsign.dxd.elements.meta;

import lombok.Data;
import org.simpleframework.xml.Element;

@Data
@Element(name="meta")
public class DxdMeta {

  @Element(name="diagrams")
  private DxdMetaDiagrams dxdMetaDiagrams;

  @Element(name="database")
  private DxdMetaDatabase dxdMetaDatabase;

  @Element(name="codeGeneration")
  private DxdMetaCodeGeneration dxdMetaCodeGeneration;
}
