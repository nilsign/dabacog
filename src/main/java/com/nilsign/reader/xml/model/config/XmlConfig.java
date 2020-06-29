package com.nilsign.reader.xml.model.config;

import lombok.Data;
import org.simpleframework.xml.Element;

@Data
@Element(name = "config")
public class XmlConfig {

  @Element(required = false)
  private XmlDiagramsConfig diagramsConfig;

  public String toString(String indentation) {
      return new StringBuffer()
          .append(String.format("%s%s\n", indentation, XmlConfig.class.getSimpleName()))
          .append(diagramsConfig.toString(String.format("%s\t", indentation)))
          .toString();
  }

  public String toString() {
    return toString("");
  }
}
