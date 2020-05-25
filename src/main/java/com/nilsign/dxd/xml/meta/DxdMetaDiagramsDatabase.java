package com.nilsign.dxd.xml.meta;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "database")
public class DxdMetaDiagramsDatabase {

  @Attribute(required = false)
  private String diagramName = "Dabacog - Database Scheme Diagram ";

  @Attribute(required = false)
  private boolean primaryKeyFieldPorts = false;

  @Attribute(required = false)
  private boolean foreignKeyFieldPorts = true;
}
