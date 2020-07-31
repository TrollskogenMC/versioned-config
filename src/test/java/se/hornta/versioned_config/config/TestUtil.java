package se.hornta.versioned_config.config;

import se.hornta.versioned_config.Util;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TestUtil {
  @Test
  public void Test_getLeafs() {
    YamlConfiguration c = new YamlConfiguration();
    c.set("a", 1);
    c.set("a.b", 1);
    c.set("a.b.c", 1);
    c.set("foo.bar", 1);
    Set<String> result = Util.getLeafs(c);
    Set<String> expected = new HashSet<>();
    expected.add("a.b.c");
    expected.add("foo.bar");
    Assert.assertEquals(expected, result);
  }
}
