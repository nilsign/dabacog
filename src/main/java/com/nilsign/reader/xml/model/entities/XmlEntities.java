package com.nilsign.reader.xml.model.entities;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Data
@Element(name = "entities")
public class XmlEntities {

  @ElementList(inline = true, entry = "class")
  private List<XmlClass> classes;
}
