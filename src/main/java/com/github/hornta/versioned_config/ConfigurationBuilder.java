package com.github.hornta.versioned_config;

import org.bukkit.plugin.PluginBase;

import java.io.File;
import java.util.LinkedList;

public class ConfigurationBuilder<T extends Enum<T>> {
  private final PluginBase plugin;
  private final File file;
  private final LinkedList<IConfigVersion<T>> versions;
  private String versionValidationError;

  public ConfigurationBuilder(PluginBase plugin, File file) {
    this.plugin = plugin;
    this.file = file;
    this.versions = new LinkedList<>();
  }

  public void addVersion(com.github.hornta.versioned_config.IConfigVersion<T> version) {
    versions.add(version);
  }

  public Configuration<T> run() {
    versions.sort(new VersionSorter());
    if(!validateVersions()) {
      plugin.getLogger().severe("Failed validation: " + versionValidationError);
      return null;
    }
    com.github.hornta.versioned_config.Configuration<T> configuration = new com.github.hornta.versioned_config.Configuration<>(plugin, file);
    configuration.validate();
    plugin.getLogger().info("Applying versions for " + file.getName());
    for (com.github.hornta.versioned_config.IConfigVersion<T> migration : versions) {
      Patch<T> patch = migration.migrate(configuration);
      boolean validationResult = patch.validate();
      if(!validationResult) {
        plugin.getLogger().severe(patch.getValidationError());
        return null;
      }
      configuration.apply(patch);
      configuration.setVersion(migration.version());
      if(configuration.getVersion() == migration.version()) {
        configuration.applyValues();
        plugin.getLogger().info("Apply values from file to version " + migration.version());
      }
      plugin.getLogger().info("Applied version " + migration.version() + " onto " + file.getName());
    }
    configuration.validate();
    configuration.cleanup();
    configuration.persistToFile();
    return configuration;
  }

  private boolean validateVersions() {
    plugin.getLogger().info("Validating versions");
    for(int i = 0; i < versions.size(); ++i) {
      if(versions.get(i).version() != i + 1) {
        versionValidationError = "Wrong version for " + versions.get(i).getClass().getName() + ". Initial version must be 1 increased by 1 for every version.";
        return false;
      }
    }

    plugin.getLogger().info("Validation OK");

    return true;
  }
}
