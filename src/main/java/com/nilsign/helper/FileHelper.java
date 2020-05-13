package com.nilsign.helper;

import lombok.NonNull;

import java.io.File;
import java.io.IOException;

public final class FileHelper {

  private FileHelper() {
  }

  public static String normalizePath(@NonNull String path) {
    String normalizedPath = path.trim().replaceAll("[/\\\\]", "\\" + File.separator);
    if (normalizedPath.charAt(normalizedPath.length() - 1) != File.separatorChar
        && new File(path).isDirectory()) {
      normalizedPath += File.separator;
    }
    return normalizedPath;
  }

  public static File createFileIfNotExists(@NonNull String filePath) throws IOException {
    File file = new File(normalizePath(filePath));
    if (!file.exists()) {
      file.createNewFile();
    }
    return file;
  }

  public static String createDirectoriesIfNotExist(@NonNull String directoryPath) {
    File directory = new File(FileHelper.normalizePath(directoryPath));
    if (!directory.exists()) {
      directory.mkdirs();
    }
    return directory.getPath();
  }

  public static void deleteFileIfExists(@NonNull String filePath) {
    File file = new File(normalizePath(filePath));
    if (!file.exists() || file.isDirectory()) {
      return;
    }
    file.delete();
  }
}
