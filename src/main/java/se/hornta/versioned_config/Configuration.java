package se.hornta.versioned_config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Configuration<T extends Enum<T>> {
  private final NodeMap<T> nodes2;
  private final File file;
  private YamlConfiguration configuration;
  private final List<Migration<T>> migrations;
  private Snapshot<T> snapshot;

  protected static final String VERSION_FIELD = "version";

  public Configuration(File file, List<Migration<T>> migrations) {
    this.file = file;
    this.nodes2 = new NodeMap();
    this.migrations = migrations;
  }

  public void reload() throws ConfigurationException {
    try {
      // 1. Load configuration file or create a fresh if not exist
      loadOrCreateConfigurationFromFile();

      // 2. Validate the configuration object
      validateConfiguration();

      // 3. Apply patches
      int currentCfgVersion = configuration.getInt(VERSION_FIELD);
      YamlConfiguration defaultSnapshotCfg = new YamlConfiguration();
      defaultSnapshotCfg.set(VERSION_FIELD, 0);
      NodeMap<T> defaultSnapshotNodes = getNodeMapBeforeVersion(currentCfgVersion, defaultSnapshotCfg);
      Snapshot<T> defaultSnapshot = new Snapshot<>(defaultSnapshotNodes, defaultSnapshotCfg);
      defaultSnapshot.applyConfiguration(configuration);
      for (Migration<T> migration : migrations) {
        if(migration.getVersion() > currentCfgVersion) {
          Patch<T> patch = migration.createPatch(defaultSnapshot);
          defaultSnapshot.update(patch);
          defaultSnapshot.getConfiguration().set(VERSION_FIELD, migration.getVersion());
        }
      }

      if(!migrations.isEmpty()) {
        defaultSnapshot.getConfiguration().set(VERSION_FIELD, migrations.get(migrations.size() - 1).getVersion());
      }
      snapshot = defaultSnapshot;
      configuration = snapshot.getConfiguration();

      // 4. Clean up configuration from unused values
      cleanup();

      // 5. Save configuration to file
      persistToFile();
    } catch (IOException | InvalidConfigurationException e) {
      throw new ConfigurationException(e);
    }
  }

  public <E> E get(T id) {
    return snapshot.get(id);
  }

  private void persistToFile() throws IOException {
    configuration.save(file);
  }

  private void loadOrCreateConfigurationFromFile() throws IOException, InvalidConfigurationException {
    if (file.exists()) {
      configuration = new YamlConfiguration();
      configuration.load(file);
    } else {
      configuration = new YamlConfiguration();
      configuration.set(VERSION_FIELD, 0);
    }
  }

  private void validateConfiguration() throws ConfigurationException {
    if(!configuration.isInt(VERSION_FIELD)) {
      throw new ConfigurationException("Expected path `version` value in %s", file.getName());
    }

    int version = configuration.getInt(VERSION_FIELD);

    if(version > 0) {
      Set<Integer> versions = migrations.stream().map(Migration::getVersion).collect(Collectors.toSet());
      if (!versions.contains(version)) {
        throw new ConfigurationException("Configuration file has version %d but such a version does not exist", version);
      }
    }

    NodeMap<T> snapshotNodes = new NodeMap<>();
    YamlConfiguration snapshotConfig = new YamlConfiguration();
    snapshotConfig.set(VERSION_FIELD, 0);
    Snapshot<T> snapshot = new Snapshot<>(snapshotNodes, snapshotConfig);

    NodeMap<T> nodesUsedInValidation = null;
    for(Migration<T> migration : migrations) {
      Patch<T> patch = migration.createPatch(snapshot);
      snapshot.update(patch);
      if(migration.getVersion() <= version) {
        nodesUsedInValidation = new NodeMap<>(snapshot.getNodes());
      }
    }
    if(nodesUsedInValidation == null) {
      nodesUsedInValidation = new NodeMap<>(snapshot.getNodes());
    }

    if(version == 0) {
      return;
    }

    Set<String> leafs = Util.getLeafs(configuration);

    // Validate nodes against configuration
    for(Node<T> node : nodesUsedInValidation) {
      if(!configuration.isSet(node.getPath())) {
        // Configuration is missing a path
        throw new ConfigurationException("Expected path %s to be found in configuration but none was set", node.getPath());
      }

      testType(node);
    }

    // Validate configuration against nodes
    for(String path : leafs) {
      if(path.equals(VERSION_FIELD)) {
        continue;
      }
      boolean hasPath = false;
      Node<T> foundNode = null;
      for(Node<T> node : nodesUsedInValidation) {
        if(node.getPath().equals(path)) {
          foundNode = node;
          hasPath = true;
          break;
        }
      }

      if(!hasPath) {
        // Configuration file contains an unknown path and should be removed
        throw new ConfigurationException("Found unexpected path %s in configuration", path);
      }

      testType(foundNode);
    }
  }

  private NodeMap<T> getNodeMapBeforeVersion(int version, YamlConfiguration cfg) {
    cfg.set(VERSION_FIELD, 0);
    NodeMap<T> nodeMap = new NodeMap<>();
    Snapshot<T> snapshot = new Snapshot<>(nodeMap, cfg);
    for(Migration<T> migration : migrations) {
      if(migration.getVersion() > version) {
        break;
      }
      Patch<T> patch = migration.createPatch(new Snapshot<>(nodeMap, cfg));
      snapshot.update(patch);
    }
    return nodeMap;
  }

  private void testType(Node<T> node) throws ConfigurationException {
    Object o = configuration.get(node.getPath());
    if(!node.getType().isCorrectType(o)) {
      // Configuration file contains a known path but with an incorrect type
      throw new ConfigurationException("Expected path %s in configuration to be of type %s but found %s", node.getPath(), node.getType().name(), o.getClass());
    }
  }

  protected void cleanup() {
    Set<String> leafs = Util.getLeafs(configuration);
    leafs.remove(VERSION_FIELD);

    for(String path : leafs) {
      tryDeletePathRecursively(path);
    }
  }

  private void tryDeletePathRecursively(String path) {
    if(hasPath(path)) {
      return;
    }
    configuration.set(path, null);
    int separatorIndex = path.lastIndexOf('.');
    if(separatorIndex != -1) {
      tryDeletePathRecursively(path.substring(0, separatorIndex));
    }
  }

  protected boolean hasPath(String path) {
    for (Node<T> value : snapshot.getNodes()) {
      if (value.getPath().equals(path)) {
        return true;
      }
    }
    return false;
  }
}
