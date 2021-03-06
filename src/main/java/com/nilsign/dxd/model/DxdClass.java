package com.nilsign.dxd.model;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
public final class DxdClass {

  @NonNull
  private final String name;

  @NonNull
  private final ImmutableList<DxdField> fields;

  public String toString(@NonNull String indentation) {
    StringBuffer output = new StringBuffer()
        .append(String.format("%s%s [%s: %s]\n",
            indentation,
            DxdClass.class.getSimpleName(),
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
