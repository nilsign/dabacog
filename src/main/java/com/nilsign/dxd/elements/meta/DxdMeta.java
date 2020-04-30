package com.nilsign.dxd.elements.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Element;

@Getter
@Setter
@ToString
@Element(name="meta")
public class DxdMeta {

  @Element(name="diagrams")
  private DxdMetaDiagrams dxdMetaDiagrams;

  @Element(name="database")
  private DxdMetaDatabase dxdMetaDatabase;

  @Element(name="codeGeneration")
  private DxdMetaCodeGeneration dxdMetaCodeGeneration;
}
