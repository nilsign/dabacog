package com.nilsign.reader.xml.model;

import com.nilsign.reader.xml.model.config.XmlConfig;
import com.nilsign.reader.xml.model.entities.XmlEntities;
import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(name="dxd")
public class XmlModel {

  @Attribute
  private String name;

  @Element
  private XmlConfig config;

  @Element
  private XmlEntities entities;
}
