package com.nilsign.dxd.xml.meta;

import lombok.Data;
import org.simpleframework.xml.Element;

@Data
@Element(name="meta")
public class DxdMeta {


  @Element(name="diagrams")
  private DxdMetaDiagrams metaDiagrams;

  @Element(name="database")
  private DxdMetaDatabase metaDatabase;

  @Element(name="codeGeneration")
  private DxdMetaCodeGeneration metaCodeGeneration;
}
