package com.nilsign.reader.xml.model.config;

import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Element;

@Data
@Element(name = "config")
public final class XmlConfig {

  @Element(required = false)
  private XmlDiagramsConfig diagramsConfig;

  @Element(required = false)
  private XmlSqlConfig sqlConfig;

  public String toString(@NonNull String indentation) {
      return new StringBuffer()
          .append(String.format("%s%s\n", indentation, XmlConfig.class.getSimpleName()))
          .append(diagramsConfig.toString(String.format("%s\t", indentation)))
          .append(sqlConfig.toString(String.format("%s\t", indentation)))
          .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
