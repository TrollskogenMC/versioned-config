package com.github.hornta.versioned_config;

import java.util.Comparator;

public class PatchSorter<T extends Enum<T>> implements Comparator<Patch<T>> {
  @Override
  public int compare(Patch<T> o1, Patch<T> o2) {
    return o1.getVersion()- o2.getVersion();
  }
}
