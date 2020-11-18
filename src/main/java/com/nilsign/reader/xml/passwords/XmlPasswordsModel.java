package com.nilsign.reader.xml.passwords;

import lombok.Data;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Data
@Root(name="pxd")
public class XmlPasswordsModel {

  @ElementList(inline = true, entry = "password")
  private List<XmlPassword> passwords;

  @Override
  public String toString() {
    StringBuffer output = new StringBuffer()
        .append(String.format("\t%s\n", XmlPasswordsModel.class.getSimpleName()));
    passwords.forEach(password -> output.append(password.toString("\t\t")));
    return output.toString();
  }
}
