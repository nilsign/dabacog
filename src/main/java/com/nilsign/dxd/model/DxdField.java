package com.nilsign.dxd.model;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
public final class DxdField {

  @NonNull
  private final DxdFieldType type;

  private final String name;

  private final DxdFieldRelationType relationType;

  private final boolean indexed;

  private final boolean unique;

  private final boolean nullable;

  // TODO(nilsheumer): Add support for full test search.
  // https://hackernoon.com/how-useful-is-postgresql-full-text-search-u39242fi
  private final boolean fts;

  private final String defaultValue;

  public boolean hasName() {
    return name != null;
  }

  public boolean hasRelation() {
    return relationType != null;
  }

  public boolean hasDefaultValue() {
    return defaultValue != null;
  }

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s [%s%s%s]\n",
            indentation,
            DxdField.class.getSimpleName(),
            String.format("Type: %s", type.getObjectName()),
            String.format(" - Name: %s", name),
            relationType != null
                ? String.format(" - Relation: %s", relationType)
                : ""))
        .toString();
  }

  public String toString() {
    return toString("");
  }
}
