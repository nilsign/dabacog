package com.nilsign.dxd.xml;

import com.nilsign.dxd.xml.entities.DxdEntities;
import com.nilsign.dxd.xml.meta.DxdMeta;
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
  private DxdMeta meta;

  @Element(name="entities")
  private DxdEntities entities;
}
