package com.nilsign.dxd;

import lombok.NonNull;
import org.simpleframework.xml.transform.Transform;

public class DxdEnumTransformer implements Transform<Enum> {

  private final Class type;

  public DxdEnumTransformer(@NonNull Class type) {
    this.type = type;
  }

  public Enum read(@NonNull String value) {
    for (Object object : type.getEnumConstants()) {
      if (object.toString().equals(value.toUpperCase())) {
        return (Enum) object;
      }
    }
    return null;
  }

  public String write(@NonNull Enum value) {
    return value.toString().toLowerCase();
  }
}
