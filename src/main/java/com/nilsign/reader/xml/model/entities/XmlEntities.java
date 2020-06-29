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

  public String toString(String indentation) {
    StringBuffer output = new StringBuffer()
        .append(String.format("%s%s\n", indentation, XmlEntities.class.getSimpleName()));
    classes.forEach(aClass
        -> output.append(aClass.toString(String.format("%s\t", indentation))));
    return output.toString();
  }

  public String toString() {
    return toString("");
  }
}
