package com.nilsign.reader.xml;

import com.nilsign.reader.xml.model.XmlModel;
import lombok.NonNull;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

/**
 * A class that reads the Dabacog Xml description files.
 */
public class XmlReader {

  private final String dxdFilePath;

  private XmlReader(@NonNull String dxdFilePath) {
    this.dxdFilePath = dxdFilePath;
  }

  public static XmlModel run(@NonNull String dxdFilePath) throws XmlReaderException {
    Serializer serializer = new Persister();
    File source = new File(dxdFilePath);
    try {
      return serializer.read(XmlModel.class, source);
    } catch(Exception e) {
      throw new XmlReaderException(e.getMessage(), e);
    }
  }
}
