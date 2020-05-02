package com.github.hornta.versioned_config.config;

import com.github.hornta.versioned_config.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginBase;
import org.bukkit.util.Vector;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class ConfigurationSpec {
  private PluginBase plugin;
  private File file;
  private Configuration<ConfigurationEnum> configuration;

  @Before
  public void beforeEach() {
    PowerMockito.mockStatic(Bukkit.class);
    plugin = new FakeJavaPlugin();
    Mockito.when(Bukkit.getItemFactory()).thenReturn(plugin.getServer().getItemFactory());
    file = new File(plugin.getDataFolder(), "config.yml");
    ConfigurationBuilder<ConfigurationEnum> cb = new ConfigurationBuilder<>(plugin, file);
    cb.addVersion(new IConfigVersion<ConfigurationEnum>() {
      @Override
      public int version() {
        return 1;
      }

      @Override
      public Patch<ConfigurationEnum> migrate(Configuration<ConfigurationEnum> configuration) {
        Patch<ConfigurationEnum> p = new Patch<>();
        p.set(ConfigurationEnum.MY_BOOLEAN, "my_boolean", true, Type.BOOLEAN);
        p.set(ConfigurationEnum.MY_COLOR, "my_color", Color.BLUE, Type.COLOR);
        p.set(ConfigurationEnum.MY_DOUBLE, "my_double", 3.14, Type.DOUBLE);
        p.set(ConfigurationEnum.MY_INTEGER, "my_integer", 7, Type.INTEGER);
        p.set(ConfigurationEnum.MY_ITEM_STACK, "my_item_stack", new ItemStack(Material.INK_SAC), Type.ITEM_STACK);
        p.set(ConfigurationEnum.MY_LIST, "my_list", Arrays.asList("diamond", "iron", "stone"), Type.LIST);
        p.set(ConfigurationEnum.MY_LONG, "my_long", 3L, Type.LONG);
        p.set(ConfigurationEnum.MY_OFFLINE_PLAYER, "my_offline_player", new FakeOfflinePlayer(), Type.OFFLINE_PLAYER);
        p.set(ConfigurationEnum.MY_STRING, "my_string", "vallmo", Type.STRING);
        p.set(ConfigurationEnum.MY_VECTOR, "my_vector", new Vector(1, 2, 3), Type.VECTOR);
        return p;
      }
    });
    configuration = cb.run();
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
  public void GetInteger() {
    Object recv = configuration.get(ConfigurationEnum.MY_INTEGER);
    Assert.assertEquals(recv.getClass(), Integer.class);
  }

  private enum ConfigurationEnum {
    MY_BOOLEAN,
    MY_COLOR,
    MY_DOUBLE,
    MY_INTEGER,
    MY_ITEM_STACK,
    MY_LIST,
    MY_LONG,
    MY_OFFLINE_PLAYER,
    MY_SET,
    MY_STRING,
    MY_VECTOR
  }
}
