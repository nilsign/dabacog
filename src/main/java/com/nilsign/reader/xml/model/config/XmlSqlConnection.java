package com.nilsign.reader.xml.model.config;

import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "sqlConnection")
public class XmlSqlConnection {

  @Attribute
  private String environmentName;

  @Attribute
  private String url;

  @Attribute
  private int port;

  @Attribute
  private String databaseName;

  @Attribute
  private String user;

  @Attribute
  private String password;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, XmlSqlConnection.class.getSimpleName()))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "EnvironmentName",
            environmentName))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "Url",
            url))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "Port",
            port))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "DatabaseName",
            databaseName))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "User",
            user))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "password",
            password))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
