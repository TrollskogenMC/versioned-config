package com.github.hornta.versioned_config;

public interface IConfigVersion<T extends Enum<T>> {
  int version();
  Patch<T> migrate(Configuration<T> configuration);
}
