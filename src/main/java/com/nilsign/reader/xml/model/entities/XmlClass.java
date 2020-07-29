package com.nilsign.reader.xml.model.entities;

import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Data
@Element(name = "class")
public final class XmlClass {

  @Attribute
  private String name;

  @ElementList(inline = true, entry = "field")
  private List<XmlField> fields;

  public String toString(@NonNull String indentation) {
    StringBuffer output = new StringBuffer()
        .append(String.format("%s%s [%s: %s]\n",
            indentation,
            XmlClass.class.getSimpleName(),
            "Name",
            name));
    fields.forEach(field
        -> output.append(field.toString(String.format("%s\t", indentation))));
    return output.toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
