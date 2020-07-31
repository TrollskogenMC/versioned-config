package se.hornta.versioned_config.config;

import se.hornta.versioned_config.*;
import se.hornta.versioned_config.ConfigurationException;
import se.hornta.versioned_config.config.mocks.FakeOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import se.hornta.versioned_config.Configuration;
import se.hornta.versioned_config.ConfigurationBuilder;
import se.hornta.versioned_config.Migration;
import se.hornta.versioned_config.Patch;
import se.hornta.versioned_config.Type;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class ConfigurationSpec {
  private File file;
  private Configuration<ConfigurationEnum> configuration;

  @Before
  public void beforeEach() throws ConfigurationException {
    PowerMockito.mockStatic(Bukkit.class);
    file = new File("config.yml");
    file.delete();
    ConfigurationBuilder<ConfigurationEnum> cb = new ConfigurationBuilder<>(file);

    cb.addMigration(new Migration<>(1, () -> {
      Patch<ConfigurationEnum> p = new Patch<>();
      p.set(ConfigurationEnum.MY_BOOLEAN, "my_boolean", true, Type.BOOLEAN);
      p.set(ConfigurationEnum.MY_COLOR, "my_color", Color.BLUE, Type.COLOR);
      p.set(ConfigurationEnum.MY_DOUBLE, "my_double", 3.14, Type.DOUBLE);
      p.set(ConfigurationEnum.MY_INTEGER, "my_integer", 7, Type.INTEGER);
      p.set(ConfigurationEnum.MY_LIST, "my_list", Arrays.asList("diamond", "iron", "stone"), Type.LIST);
      p.set(ConfigurationEnum.MY_LONG, "my_long", 3L, Type.LONG);
      p.set(ConfigurationEnum.MY_OFFLINE_PLAYER, "my_offline_player", new FakeOfflinePlayer(), Type.OFFLINE_PLAYER);
      p.set(ConfigurationEnum.MY_STRING, "my_string", "vallmo", Type.STRING);
      p.set(ConfigurationEnum.MY_VECTOR, "my_vector", new Vector(1, 2, 3), Type.VECTOR);
      return p;
    }));
    configuration = cb.create();
  }

  @After
  public void afterEach() {
    boolean ret = file.delete();
    if(!ret) {
      throw new Error();
    }
  }

  @Test
  public void GetInteger() {
    Object recv = configuration.get(ConfigurationEnum.MY_INTEGER);
    Assert.assertEquals(recv.getClass(), Integer.class);
  }

  private enum ConfigurationEnum {
    MY_BOOLEAN,
    MY_COLOR,
    MY_DOUBLE,
    MY_INTEGER,
    MY_LIST,
    MY_LONG,
    MY_OFFLINE_PLAYER,
    MY_STRING,
    MY_VECTOR
  }
}
