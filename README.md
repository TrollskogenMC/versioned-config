# versioned-config
A Bukkit helper for creating versioned configuration files. It can be seen as a smart wrapper around the [FileConfiguration](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/configuration/file/FileConfiguration.html) object.

Usage
--

- [Install](#install)
- [Getting started](#getting-started)
  
## Install
This package is available on Github Packages. Read more [here](https://help.github.com/en/packages/publishing-and-managing-packages/installing-a-package).
```xml
<dependency>
  <groupId>com.github.hornta</groupId>
  <artifactId>versioned-config</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
<br />

## Getting started
Here we've created a plugin with a configuration object. The enum `ConfigKey` is used to access configuration values.

```java
// MyPlugin.java
class MyPlugin {
  private Configuration pluginConfig;

  @Override
  void onEnable() {
    File file = new File(getDataFolder(), "config.yml");
    ConfigurationBuilder<ConfigKey> cb = new ConfigurationBuilder<>(file);
    cb.addMigration(new Migration(1, () -> {
      Patch<ConfigKey> patch = new Patch<>();
      patch.set(ConfigKey.FOO, "foo", true, Type.BOOLEAN);
      return patch;
    }));
    
    cb.addMigration(new Migration(2, () -> {
      Patch<ConfigKey> patch = new Patch<>();
      patch.set(ConfigKey.BAR, "bar", "myValue", Type.STRING);
      return patch;
    }));
    
    try {
     pluginConfig = cb.create();
    } catch (ConfigurationException e) {
      getLogger().log(Level.SEVERE, "Failed to setup configuration", e);
      setEnabled(false);
    }
  }
}

// ConfigKey.java
enum ConfigKey {
  FOO,
  BAR
}
```
