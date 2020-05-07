package com.github.hornta.versioned_config.config;

import com.github.hornta.versioned_config.Patch;
import com.github.hornta.versioned_config.PatchSorter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class Test_PatchSorter {

  @Test
  public void Test_VersionSorter_Sort() {
    Patch<VersionSorterEnum> v1 = new Patch<>(1);
    Patch<VersionSorterEnum> v2 = new Patch<>(2);
    Patch<VersionSorterEnum> v3 = new Patch<>(3);

    List<Patch<VersionSorterEnum>> sorted = Arrays.asList(v3, v2, v1);
    sorted.sort(new PatchSorter<>());

    Assert.assertEquals(v1.getVersion(), sorted.get(0).getVersion());
    Assert.assertEquals(v2.getVersion(), sorted.get(1).getVersion());
    Assert.assertEquals(v3.getVersion(), sorted.get(2).getVersion());
  }

  private enum VersionSorterEnum {}
}
