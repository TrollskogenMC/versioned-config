package com.github.hornta.versioned_config.config.migrations;

import com.github.hornta.versioned_config.Patch;
import com.github.hornta.versioned_config.Type;
import com.github.hornta.versioned_config.Configuration;
import com.github.hornta.versioned_config.IConfigVersion;
import com.github.hornta.versioned_config.config.MyEnum;

public class InitialVersion implements IConfigVersion<MyEnum> {
  @Override
  public int version() {
    return 1;
  }

  @Override
  public Patch<MyEnum> migrate(Configuration<MyEnum> configuration) {
    Patch<MyEnum> patch = new Patch<>();
    patch.set(MyEnum.FOO, "homes", 1, Type.INTEGER);
    return patch;
  }
}
