package com.nilsign.reader.xml.passwords;

import lombok.Data;
import lombok.NonNull;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Data
@Element(name = "password")
public final class XmlPassword {

  @Attribute
  private String environment;

  @Attribute
  private String name;

  @Attribute
  private String user;

  @Attribute
  private String password;

  public String toString(@NonNull String indentation) {
    return new StringBuffer()
        .append(String.format("%s%s\n", indentation, XmlPassword.class.getSimpleName()))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "Environment",
            environment))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "Name",
            name))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "User",
            user))
        .append(String.format("%s\t%s: %s\n",
            indentation,
            "Password",
            "*****"))
        .toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}
