package com.github.hornta.versioned_config;

public interface IMigration<T extends Enum<T>> {
  int getVersion();
  Patch<T> createPatch(Snapshot<T> snapshot);
}
