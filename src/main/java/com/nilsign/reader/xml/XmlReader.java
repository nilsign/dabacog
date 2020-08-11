package com.nilsign.reader.xml;

import com.nilsign.reader.xml.model.XmlModel;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.File;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class XmlReader {

  @NonNull
  private final String dxdFilePath;

  public static XmlModel run(@NonNull String dxdFilePath) {
    try {
      Serializer serializer = new Persister();
      File source = new File(dxdFilePath);
      return serializer.read(XmlModel.class, source);
    } catch (Exception e) {
      throw new XmlReaderException(e);
    }
  }
}
