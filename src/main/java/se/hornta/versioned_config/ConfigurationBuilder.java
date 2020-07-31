package se.hornta.versioned_config;

import java.io.File;
import java.util.LinkedList;

public class ConfigurationBuilder<T extends Enum<T>> {
  private final File file;
  private final LinkedList<Migration<T>> migrations;

  public ConfigurationBuilder(File file) {
    this.file = file;
    this.migrations = new LinkedList<>();
  }

  public void addMigration(Migration<T> migration) {
    migrations.add(migration);
  }

  public Configuration<T> create() throws ConfigurationException {
    // 1. Sort patches from lowest version to highest
    // patches.sort(new PatchSorter<>());

    // 1. Sort migrations from lowest version to highest
    migrations.sort(new MigrationSorter<>());

    // 2. Validate patches
    //validatePatchVersions();
    validateMigrations();

    // 3. Create configuration with file and patches
    Configuration<T> configuration = new Configuration<>(file, migrations);

    // 4. Perform a reload which runs the migrations
    configuration.reload();

    return configuration;
  }

  private void validateMigrations() throws ConfigurationException {
    for(int i = 0; i < migrations.size(); ++i) {
      if(migrations.get(i).getVersion() != i + 1) {
        throw new ConfigurationException("Wrong version for %s. Version must start at 1 increased by 1 for every migration.", migrations.get(i).getClass().getName());
      }
    }
  }
}
