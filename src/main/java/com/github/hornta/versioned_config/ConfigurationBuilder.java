package com.github.hornta.versioned_config;

import java.io.File;
import java.util.LinkedList;

public class ConfigurationBuilder<T extends Enum<T>> {
  private final File file;
  private final LinkedList<Patch<T>> patches;

  public ConfigurationBuilder(File file) {
    this.file = file;
    this.patches = new LinkedList<>();
  }

  public void addPatch(Patch<T> patch) {
    patches.add(patch);
  }

  public Configuration<T> create() throws ConfigurationException {
    // 1. Sort patches from lowest version to highest
    patches.sort(new PatchSorter<>());

    // 2. Validate patches
    validatePatchVersions();

    // 3. Create configuration with file and patches
    Configuration<T> configuration = new Configuration<>(file, patches);

    // 4. Perform a reload which applies the patches
    configuration.reload();

    return configuration;
  }

  private void validatePatchVersions() throws ConfigurationException {
    for(int i = 0; i < patches.size(); ++i) {
      if(patches.get(i).getVersion() != i + 1) {
        throw new ConfigurationException("Wrong version for %s. Initial version must be 1 increased by 1 for every version.", patches.get(i).getClass().getName());
      }
      patches.get(i).validate();
    }
  }
}
