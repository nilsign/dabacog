package com.nilsign.reader.xml.model.config;

import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "sqlConfig")
public class XmlSqlConfig {

  @Attribute(required = false)
  private String sqlOutputPath;

  @Attribute(required = false)
  private boolean sqlGlobalSequence;

  @Attribute(required = false)
  private boolean sqlDumpDatabase;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, XmlSqlConfig.class.getSimpleName()))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "SqlOutputPath",
            sqlOutputPath))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "SqlGlobalSequence",
            sqlGlobalSequence))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "SqlDumpDatabase",
            sqlDumpDatabase))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
