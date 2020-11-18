package com.nilsign.reader.xml;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class XmlReader {

  public static <T> T run(@NonNull String xmlFilePath, Class<T> model) {
    try {
      Serializer serializer = new Persister();
      File source = new File(xmlFilePath);
      return serializer.read(model, source);
    } catch (Exception e) {
      throw new XmlReaderException(e, xmlFilePath);
    }
  }
}
