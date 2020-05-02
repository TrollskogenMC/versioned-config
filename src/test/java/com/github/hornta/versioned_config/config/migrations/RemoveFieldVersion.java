package com.github.hornta.versioned_config.config.migrations;

import com.github.hornta.versioned_config.Patch;
import com.github.hornta.versioned_config.Configuration;
import com.github.hornta.versioned_config.IConfigVersion;
import com.github.hornta.versioned_config.config.MyEnum;

public class RemoveFieldVersion implements IConfigVersion<MyEnum> {
  @Override
  public int version() {
    return 3;
  }

  @Override
  public Patch<MyEnum> migrate(Configuration<MyEnum> configuration) {
    Patch<MyEnum> patch = new Patch<>();
    patch.unset(MyEnum.FOO);
    return patch;
  }
}
