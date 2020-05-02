package com.github.hornta.versioned_config.config;

import com.github.hornta.versioned_config.*;
import com.github.hornta.versioned_config.config.migrations.AddFieldVersion;
import com.github.hornta.versioned_config.config.migrations.InitialVersion;
import com.github.hornta.versioned_config.config.migrations.RemoveFieldVersion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginBase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class TestConfigurationBuilder {
  private PluginBase plugin;
  private File file;

  @Before
  public void beforeEach() {
    plugin = new FakeJavaPlugin();
    file = new File(plugin.getDataFolder(), "config.yml");
  }

  @After
  public void afterEach() {
    boolean ret = file.delete();
    if(!ret) {
      throw new Error();
    }
    plugin.getDataFolder().delete();
  }

  @Test
  public void createsEmptyFile() throws IOException {
    Assert.assertFalse(file.exists());
    ConfigurationBuilder cb = new ConfigurationBuilder(plugin, file);
    cb.run();
    Assert.assertTrue(file.exists());
    List<String> contents = Files.readAllLines(file.toPath(), Charset.defaultCharset());
    Assert.assertEquals(0, contents.size());
  }

  @Test
  public void buildUp_NoFile() throws IOException {
    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(plugin, file);
    cb.addVersion(new InitialVersion());
    cb.addVersion(new AddFieldVersion());
    cb.addVersion(new RemoveFieldVersion());
    cb.run();

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
  public void buildUp_File() throws IOException {
    YamlConfiguration configuration = new YamlConfiguration();
    configuration.set("version", 3);
    configuration.set("second_field", "a stronger value");
    configuration.save(file);

    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(plugin, file);
    cb.addVersion(new InitialVersion());
    cb.addVersion(new AddFieldVersion());
    cb.addVersion(new RemoveFieldVersion());
    cb.run();

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
  public void returnsQueryableConfiguration() throws IOException {
    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(plugin, file);
    cb.addVersion(new IConfigVersion<MyEnum>() {
      @Override
      public int version() {
        return 1;
      }

      @Override
      public Patch<MyEnum> migrate(Configuration<MyEnum> configuration) {
        Patch<MyEnum> patch = new Patch<>();
        patch.set(MyEnum.FOO, "path", "myVal", Type.STRING);
        return patch;
      }
    });
    Configuration<MyEnum> configuration = cb.run();
    Assert.assertEquals("myVal", configuration.get(MyEnum.FOO));
  }
}
