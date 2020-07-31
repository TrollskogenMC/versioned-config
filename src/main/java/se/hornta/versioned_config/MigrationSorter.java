package se.hornta.versioned_config;

import java.util.Comparator;

public class MigrationSorter<T extends Enum<T>> implements Comparator<Migration<T>> {
  @Override
  public int compare(Migration<T> o1, Migration<T> o2) {
    return o1.getVersion()- o2.getVersion();
  }
}
