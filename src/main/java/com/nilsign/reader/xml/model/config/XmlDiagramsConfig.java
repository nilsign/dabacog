package com.nilsign.reader.xml.model.config;

import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "diagramConfig")
public final class XmlDiagramsConfig {

  @Attribute(required = false)
  private String diagramDatabaseOutputPath;

  @Attribute(required = false)
  private String diagramDatabaseTitle;

  @Attribute(required = false)
  private boolean diagramDatabasePrimaryKeyFieldPorts;

  @Attribute(required = false)
  private boolean diagramDatabaseForeignKeyFieldPorts;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, XmlDiagramsConfig.class.getSimpleName()))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DiagramDatabaseOutputPath",
            diagramDatabaseOutputPath))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DiagramDatabaseTitle",
            diagramDatabaseTitle))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DiagramDatabasePrimaryKeyFieldPorts",
            diagramDatabasePrimaryKeyFieldPorts))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DiagramDatabaseForeignKeyFieldPorts",
            diagramDatabaseForeignKeyFieldPorts))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
