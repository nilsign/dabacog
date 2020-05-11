package com.nilsign.dxd.elements;

import com.nilsign.dxd.elements.entities.DxdEntities;
import com.nilsign.dxd.elements.meta.DxdMeta;
import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(name="dxd")
public class DxdModel {

  @Attribute
  private String name;

  @Element(name="meta")
  private DxdMeta dxdMeta;

  @Element(name="entities")
  private DxdEntities entities;
}
