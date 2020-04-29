package com.nilsign.dxd;

import com.nilsign.dxd.meta.DxdMetaElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Setter
@ToString
@Root(name = "dxd")
public class DxdModel {

  @Attribute
  private String name;

  @Element(name = "meta")
  private DxdMetaElement dxdMeta;

}
