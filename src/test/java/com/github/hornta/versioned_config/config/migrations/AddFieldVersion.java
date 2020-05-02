package com.github.hornta.versioned_config.config.migrations;

import com.github.hornta.versioned_config.Patch;
import com.github.hornta.versioned_config.Type;
import com.github.hornta.versioned_config.Configuration;
import com.github.hornta.versioned_config.IConfigVersion;
import com.github.hornta.versioned_config.config.MyEnum;

public class AddFieldVersion implements IConfigVersion<MyEnum> {
  @Override
  public int version() {
    return 2;
  }

  @Override
  public Patch<MyEnum> migrate(Configuration<MyEnum> configuration) {
    Patch<MyEnum> patch = new Patch<>();
    patch.set(MyEnum.BAR, "second_field", "a string value", Type.STRING);
    return patch;
  }
}
