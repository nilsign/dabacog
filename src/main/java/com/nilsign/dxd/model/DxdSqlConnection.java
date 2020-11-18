package com.nilsign.dxd.model;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
public final class DxdSqlConnection {

  @NonNull
  private String environmentName;

  @NonNull
  private String url;

  @NonNull
  private int port;

  @NonNull
  private String databaseName;

  @NonNull
  private String user;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, DxdSqlConnection.class.getSimpleName()))
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
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
