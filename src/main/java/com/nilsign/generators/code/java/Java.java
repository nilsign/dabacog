package com.nilsign.generators.code.java;

import com.nilsign.generators.Generator;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Java {

  public static String buildJavaFileName(@NonNull String name) {
    return String.format("%s.java", normalizeClassName(name));
  }

  public static String buildGeneratedByComment() {
    return String.format("// %s", Generator.buildGeneratedByComment());
  }

  public static String normalizeClassName(@NonNull String name) {
    return startUpperCased(name);
  }

  public static String normalizeFieldName(@NonNull String name) {
    return String.format("%s%s", name.substring(0, 1).toLowerCase(), name.substring(1));
  }

  public static String startUpperCased(@NonNull String name) {
    return String.format("%s%s", name.substring(0, 1).toUpperCase(), name.substring(1));
  }
}
