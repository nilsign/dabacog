package com.nilsign.misc;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
public final class Wrapper<T> {

  private T value;

  public T get() {
    return value;
  }

  public void set(T value) {
    this.value = value;
  }
}
