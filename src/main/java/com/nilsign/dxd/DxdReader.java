package com.nilsign.dxd;

import com.nilsign.dxd.elements.DxdModel;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

/**
 * A class that reads the DXD (Dabacog XML Description) files and creates the according an in-memory
 * model.
 */
public class DxdReader {

  private final String dxdFilePath;

  private DxdReader(String dxdFilePath) {
    this.dxdFilePath = dxdFilePath;
  }

  public static DxdModel run(String dxdFilePath) throws DxdReaderException {
    Serializer serializer = new Persister();
    File source = new File(dxdFilePath);

    try {
      return serializer.read(DxdModel.class, source);
    } catch(Exception e) {
      throw new DxdReaderException(e.getMessage(), e);
    }
  }
}


