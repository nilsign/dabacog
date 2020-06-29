package com.nilsign.dxd.model;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
public final class DxdClass {

  private final String name;

  @NonNull
  private final ImmutableList<DxdField> fields;
}
