package com.nilsign.dxd.elements.entities;

import org.simpleframework.xml.transform.Transform;

public class EnumTransformer implements Transform<Enum> {

  private final Class type;

  public EnumTransformer(Class type) {
    this.type = type;
  }

  public Enum read(String value) {
    for (Object object : type.getEnumConstants()) {
      if (object.toString().equals(value.toUpperCase())) {
        return (Enum) object;
      }
    }
    return null;
  }

  public String write(Enum value) {
    return value.toString().toLowerCase();
  }
}
