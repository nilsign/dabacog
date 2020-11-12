package com.nilsign.reader.xml.model.config;

import com.nilsign.dxd.model.DxdConfig;
import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

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

  @ElementList(empty = false, inline = true, entry = "sqlConnection")
  private List<XmlSqlConnection> sqlConnections;

  public String toString(@NonNull String indentation) {
    StringBuffer output = new StringBuffer()
        .append(String.format("%s%s\n", indentation, XmlSqlConfig.class.getSimpleName()))
        .append(String.format("%s\t%s: %s\n",
            indentation,
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
            sqlDropSchema));
    sqlConnections.forEach(connection
        -> output.append(connection.toString(String.format("%s\t", indentation))));
    return output.toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
