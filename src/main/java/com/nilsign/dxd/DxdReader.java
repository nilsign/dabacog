package com.nilsign.dxd;

import com.nilsign.dxd.xml.DxdModel;
import com.nilsign.dxd.xmlvaluetypes.DxdEnumTransformer;
import lombok.NonNull;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;

import java.io.File;

/**
 * A class that reads the DXD (Dabacog XML Description) files and creates the according an in-memory
 * model.
 */
public class DxdReader {

  private final String dxdFilePath;

  private DxdReader(@NonNull String dxdFilePath) {
    this.dxdFilePath = dxdFilePath;
  }

  public static DxdModel run(@NonNull String dxdFilePath) throws DxdReaderException {
    Serializer serializer = new Persister((Matcher) type -> {
        if (type.isEnum()) {
          return new DxdEnumTransformer(type);
        }
        return null;
    });
    File source = new File(dxdFilePath);
    try {
      return serializer.read(DxdModel.class, source);
    } catch(Exception e) {
      throw new DxdReaderException(e.getMessage(), e);
    }
  }
}
