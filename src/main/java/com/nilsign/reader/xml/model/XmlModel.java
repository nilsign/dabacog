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
  private String name = "Dabacog XML Description Model";

  @Element
  private XmlConfig config;

  @Element
  private XmlEntities entities;

  public String toString() {
    return new StringBuffer()
        .append(String.format("\t%s - Name: %s\n", XmlModel.class.getSimpleName(), name))
        .append(config.toString("\t\t"))
        .append(entities.toString("\t\t"))
        .toString();
  }
}
