package se.hornta.versioned_config.config;

import se.hornta.versioned_config.Migration;
import se.hornta.versioned_config.MigrationSorter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class Test_MigrationSorter {

  @Test
  public void Test_MigrationSorter_Sort() {
    Migration<MyEnum> m1 = new Migration<>(1, () -> null);
    Migration<MyEnum> m2 = new Migration<>(2, () -> null);
    Migration<MyEnum> m3 = new Migration<>(3, () -> null);

    List<Migration<MyEnum>> sorted = Arrays.asList(m3, m2, m1);
    sorted.sort(new MigrationSorter<>());

    Assert.assertEquals(m1.getVersion(), sorted.get(0).getVersion());
    Assert.assertEquals(m2.getVersion(), sorted.get(1).getVersion());
    Assert.assertEquals(m3.getVersion(), sorted.get(2).getVersion());
  }

  private enum MyEnum {}
}
