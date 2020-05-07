package com.github.hornta.versioned_config.config;

import com.github.hornta.versioned_config.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestConfigurationBuilder {
  private File file;

  @Before
  public void beforeEach() {
    file = new File("config.yml");
  }

  @After
  public void afterEach() {
    boolean ret = file.delete();
    if(!ret) {
      throw new Error();
    }
  }

  @Test
  public void createsEmptyFile() throws ConfigurationException, IOException {
    Assert.assertFalse(file.exists());
    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(file);
    cb.create();
    Assert.assertTrue(file.exists());
    List<String> contents = Files.readAllLines(file.toPath(), Charset.defaultCharset());
    Assert.assertEquals(Collections.singletonList(
      "version: 0"
    ), contents);
  }

  @Test
  public void buildUp_NoFile() throws IOException, ConfigurationException {
    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(file);

    Patch<MyEnum> patch1 = new Patch<>(1);
    patch1.set(MyEnum.FOO, "homes", 1, Type.INTEGER);

    Patch<MyEnum> patch2 = new Patch<>(2);
    patch2.set(MyEnum.BAR, "second_field", "a string value", Type.STRING);

    Patch<MyEnum> patch3 = new Patch<>(3);
    patch3.unset(MyEnum.FOO);

    cb.addPatch(patch1);
    cb.addPatch(patch2);
    cb.addPatch(patch3);
    cb.create();

    List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
    Assert.assertEquals(
      Arrays.asList(
        "version: 3",
        "second_field: a string value"
      ),
      lines
    );
  }

  @Test
  public void buildUp_Config_Exist() throws IOException, ConfigurationException {
    YamlConfiguration configuration = new YamlConfiguration();
    configuration.set("version", 3);
    configuration.set("second_field", "a stronger value");
    configuration.save(file);

    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(file);
    Patch<MyEnum> patch1 = new Patch<>(1);
    patch1.set(MyEnum.FOO, "homes", 1, Type.INTEGER);

    Patch<MyEnum> patch2 = new Patch<>(2);
    patch2.set(MyEnum.BAR, "second_field", "a string value", Type.STRING);

    Patch<MyEnum> patch3 = new Patch<>(3);
    patch3.unset(MyEnum.FOO);

    cb.addPatch(patch1);
    cb.addPatch(patch2);
    cb.addPatch(patch3);
    cb.create();

    List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
    Assert.assertEquals(
      Arrays.asList(
        "version: 3",
        "second_field: a stronger value"
      ),
      lines
    );
  }

  @Test
  public void returnsQueryableConfiguration() throws ConfigurationException {
    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(file);
    Patch<MyEnum> patch = new Patch<>(1);
    patch.set(MyEnum.FOO, "path", "myVal", Type.STRING);
    cb.addPatch(patch);
    Configuration<MyEnum> configuration = cb.create();
    Assert.assertEquals("myVal", configuration.get(MyEnum.FOO));
  }

  @Test
  public void Test_RenameField() {
    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(file);
    Patch<MyEnum> p1 = new Patch<>(1);
    p1.set(MyEnum.FOO, "field", "foo", Type.STRING);
    cb.addPatch(p1);

    Patch<MyEnum> p2 = new Patch<>(2);
    p2.set(MyEnum.BAR, "new_field", "bar", Type.STRING);
    cb.addPatch();
  }
}
