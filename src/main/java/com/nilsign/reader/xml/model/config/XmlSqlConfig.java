package com.nilsign.reader.xml.model.config;

import com.nilsign.dxd.model.DxdConfig;
import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "sqlConfig")
public class XmlSqlConfig {

  @Attribute(required = false)
  private String sqlDatabaseType = DxdConfig.DEFAULT_SQL_DATABASE_TYPE.toString();

  @Attribute(required = false)
  private String sqlOutputPath = DxdConfig.DEFAULT_SQL_OUTPUT_PATH;

  @Attribute(required = false)
  private boolean sqlGlobalSequence = DxdConfig.DEFAULT_SQL_GLOBAL_SEQUENCE;

  @Attribute(required = false)
  private boolean sqlDropSchema = DxdConfig.DEFAULT_SQL_DUMP_DATABASE;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, XmlSqlConfig.class.getSimpleName()))
        .append(String.format("%s\t%s: %s\n",
            sqlDatabaseType,
            "SqlDatabaseType",
            sqlDatabaseType))
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
            "SqlDropSchema",
            sqlDropSchema))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
