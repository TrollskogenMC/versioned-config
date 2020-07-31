package se.hornta.versioned_config.config;

import se.hornta.versioned_config.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.hornta.versioned_config.Configuration;
import se.hornta.versioned_config.ConfigurationBuilder;
import se.hornta.versioned_config.ConfigurationException;
import se.hornta.versioned_config.Migration;
import se.hornta.versioned_config.Patch;
import se.hornta.versioned_config.Snapshot;
import se.hornta.versioned_config.Type;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    cb.addMigration(new Migration<>(1, () -> {
      Patch<MyEnum> patch = new Patch<>();
      patch.set(MyEnum.FOO, "homes", 1, Type.INTEGER);
      return patch;
    }));

    cb.addMigration(new Migration<>(2, () -> {
      Patch<MyEnum> patch = new Patch<>();
      patch.set(MyEnum.BAR, "second_field", "a string value", Type.STRING);
      return patch;
    }));

    cb.addMigration(new Migration<>(3, () -> {
      Patch<MyEnum> patch = new Patch<>();
      patch.unset(MyEnum.FOO);
      return patch;
    }));
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
    cb.addMigration(new Migration<>(1, () -> {
      Patch<MyEnum> patch = new Patch<>();
      patch.set(MyEnum.FOO, "homes", 1, Type.INTEGER);
      return patch;
    }));

    cb.addMigration(new Migration<>(2, () -> {
      Patch<MyEnum> patch = new Patch<>();
      patch.set(MyEnum.BAR, "second_field", "a string value", Type.STRING);
      return patch;
    }));

    cb.addMigration(new Migration<>(3, () -> {
      Patch<MyEnum> patch = new Patch<>();
      patch.unset(MyEnum.FOO);
      return patch;
    }));
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
    cb.addMigration(new Migration<>(1, () -> {
      Patch<MyEnum> patch = new Patch<>();
      patch.set(MyEnum.FOO, "path", "myVal", Type.STRING);
      return patch;
    }));
    Configuration<MyEnum> configuration = cb.create();
    Assert.assertEquals("myVal", configuration.get(MyEnum.FOO));
  }

  @Test
  public void Test_Migrations() throws ConfigurationException, IOException {
    ConfigurationBuilder<MyEnum> cb = new ConfigurationBuilder<>(file);
    Migration<MyEnum> initial = new Migration<>(1, () -> {
      Patch<MyEnum> p = new Patch<>();
      p.set(MyEnum.FOO, "field", "foo", Type.STRING);
      return p;
    });
    Migration<MyEnum> renameField = new Migration<>(2, (Snapshot<MyEnum> c) -> {
      Patch<MyEnum> p = new Patch<>();
      p.unset(MyEnum.FOO);
      p.set(MyEnum.FOO, "new_field", c.get(MyEnum.FOO), Type.STRING);
      return p;
    });

    cb.addMigration(initial);
    cb.addMigration(renameField);
    cb.create();

    List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
    Assert.assertEquals(
      Arrays.asList(
        "version: 2",
        "new_field: foo"
      ),
      lines
    );
  }
}
