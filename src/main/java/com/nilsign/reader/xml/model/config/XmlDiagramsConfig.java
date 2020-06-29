package com.nilsign.reader.xml.model.config;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "diagrams")
public class XmlDiagramsConfig {

  @Attribute(required = false)
  private String diagramDatabaseOutputPath;

  @Attribute(required = false)
  private String diagramDatabaseTitle;

  @Attribute(required = false)
  private boolean diagramDatabasePrimaryKeyFieldPorts;

  @Attribute(required = false)
  private boolean diagramDatabaseForeignKeyFieldPorts;
}
