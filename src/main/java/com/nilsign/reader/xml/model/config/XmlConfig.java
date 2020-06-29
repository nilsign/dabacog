package com.nilsign.reader.xml.model.config;

import lombok.Data;
import org.simpleframework.xml.Element;

@Data
@Element(name = "config")
public class XmlConfig {

  @Element(required = false)
  private XmlDiagramsConfig diagramsConfig;
}
