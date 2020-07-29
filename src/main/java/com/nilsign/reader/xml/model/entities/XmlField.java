package com.nilsign.reader.xml.model.entities;

import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "field")
public final class XmlField {

  @Attribute
  private String type;

  @Attribute(required = false)
  private String name;

  @Attribute(required = false)
  private String relation;

  @Attribute(required = false)
  private boolean indexed;

  @Attribute(required = false)
  private boolean unique;

  @Attribute(required = false)
  private boolean nullable;

  // TODO(nilsheumer): Add support for full text search.
  // https://hackernoon.com/how-useful-is-postgresql-full-text-search-u39242fi
  @Attribute(required = false)
  private boolean fts;

  @Attribute(name = "default", required = false)
  private String defaultValue;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s [%s: %s - %s: %s]\n",
            indentation,
            XmlField.class.getSimpleName(),
            "Type",
            type,
            relation != null ? "Relation" : "Name",
            relation != null ? relation : name))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
