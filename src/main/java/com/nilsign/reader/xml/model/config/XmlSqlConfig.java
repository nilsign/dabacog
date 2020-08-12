package com.nilsign.reader.xml.model.config;


import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "sqlConfig")
public class XmlSqlConfig {

  @Attribute(required = false)
  private String sqlOutputPath;

  @Attribute(required = false)
  private boolean globalSequence;

  @Attribute(required = false)
  private boolean flywayFileName;

  @Attribute(required = false)
  private boolean deleteExistingSqlScripts;
}
