package com.nilsign.reader.xml.model.entities;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Data
@Element(name = "class")
public class XmlClass {

  @Attribute
  private String name;

  @ElementList(inline = true, entry = "field")
  private List<XmlField> fields;
}
