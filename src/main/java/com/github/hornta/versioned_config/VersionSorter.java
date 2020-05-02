package com.github.hornta.versioned_config;

import java.util.Comparator;

public class VersionSorter<T extends Enum<T>> implements Comparator<IConfigVersion<T>> {
  @Override
  public int compare(com.github.hornta.versioned_config.IConfigVersion o1, com.github.hornta.versioned_config.IConfigVersion o2) {
    return o1.version() - o2.version();
  }
}
