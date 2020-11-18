package com.nilsign.reader.xml.model.config;

import com.nilsign.dxd.model.DxdConfig;
import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "codeConfig")
public class XmlCodeConfig {

  @Attribute(required = false)
  private String codeType = DxdConfig.DEFAULT_CODE_DATABASE_TYPE.toString();

  @Attribute(required = false)
  private String codeOutputPath = DxdConfig.DEFAULT_CODE_OUTPUT_PATH;

  @Attribute
  private String codePackageName;

  @Attribute
  private String codePasswordsFile;

  @Attribute
  private String codePasswordsOutputPath;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, XmlCodeConfig.class.getSimpleName()))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "CodeDatabaseType",
            codeType))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "CodeOutputPath",
            codeOutputPath))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "CodePackageName",
            codePackageName))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "CodePasswordsFile",
            codePasswordsFile))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "CodePasswordsOutputPath",
            codePasswordsOutputPath))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
