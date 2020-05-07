package com.github.hornta.versioned_config.config;

import com.github.hornta.versioned_config.Patch;
import com.github.hornta.versioned_config.Type;
import org.junit.Test;

import java.util.Arrays;

public class TestPatch {

  @Test
  public void Test_Patch_set() {
    Patch<PatchEnum> p = new Patch<>(1);
    p.set(PatchEnum.A, "a", 1, Type.INTEGER);
    p.set(PatchEnum.B, "b", "value", Type.STRING);
    p.set(PatchEnum.C, "c", Arrays.asList(1, 2, 3), Type.LIST);
  }

  @Test
  public void Test_Patch_set_unset() {
    Patch<PatchEnum> p = new Patch<>(1);
    p.set(PatchEnum.A, "a", true, Type.BOOLEAN);
    p.unset(PatchEnum.A);
  }

  @Test
  public void Test_Patch_unset() {
    Patch<PatchEnum> p = new Patch<>(1);
    p.unset(PatchEnum.A);
  }

  private enum PatchEnum {
    A,
    B,
    C
  }
}
