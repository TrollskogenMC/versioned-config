package com.github.hornta.versioned_config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Configuration<T extends Enum<T>> {
  private final Map<T, Node<T>> nodes;
  private final File file;
  private YamlConfiguration configuration;
  private final List<Patch<T>> patches;
  private final Map<Integer, Map<T, Node<T>>> nodesByVersion;

  protected static final String VERSION_FIELD = "version";

  public Configuration(File file, List<Patch<T>> patches) {
    this.file = file;
    this.nodes = new LinkedHashMap<>();
    this.patches = patches;
    this.nodesByVersion = new HashMap<>();
  }

  public void reload() throws ConfigurationException {
    nodes.clear();

    try {
      // 1. Load configuration file or create a fresh if not exist
      loadOrCreateConfigurationFromFile();

      // 2. Setup patches
      setupPatches();

      // 3. Validate the configuration object
      validateConfiguration();

      // 4. Apply patches
      for (Patch<T> patch : patches) {
        apply(patch);
      }

      // 5. Clean up configuration from unused values
      cleanup();

      // 6. Save configuration to file
      persistToFile();
    } catch (IOException | InvalidConfigurationException e) {
      throw new ConfigurationException(e);
    }
  }

  public <E> E get(T key) {
    if (!nodes.containsKey(key)) {
      throw new Error("Cannot find value for key `" + key.name() + "`");
    }
    Node<T> value = nodes.get(key);
    Object obj;
    switch (value.getType()) {
      case DOUBLE:
        obj = configuration.getDouble(value.getPath());
        break;
      case LONG:
        obj = configuration.getLong(value.getPath());
        break;
      default:
        obj = configuration.get(value.getPath());
    }
    Function<Object, Object> converter = value.getConverter();
    if (converter != null) {
      return (E)converter.apply(obj);
    }
    return (E)obj;
  }

  public Set<String> getPaths() {
    Set<String> paths = new HashSet<>();
    for(Node<T> node : nodes.values()) {
      paths.add(node.getPath());
    }
    return paths;
  }

  public boolean setValue(String path, Object value) {
    return setValue(path, value, false);
  }

  public boolean setValue(String path, Object value, boolean persist) {
    Node<T> node = null;
    for(Node<T> n : nodes.values()) {
      if(n.getPath().equals(path)) {
        node = n;
        break;
      }
    }

    if(node == null) {
      return false;
    }

    if(!node.getType().isCorrectType(value)) {
      return false;
    }

    node.setCurrentValue(value);

    return true;
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

    // If version is zero the configuration is fresh so there is nothing to validate
    if(version == 0) {
      return;
    }

    if(!nodesByVersion.containsKey(version)) {
      throw new ConfigurationException("Version from file is does not exist.");
    }

    Map<T, Node<T>> nodes = nodesByVersion.get(version);
    Set<String> leafs = Util.getLeafs(configuration);

    // Validate nodes against configuration
    for(Node<T> node : nodes.values()) {
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
      for(Node<T> node : nodes.values()) {
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
    for (Node<T> value : nodes.values()) {
      if (value.getPath().equals(path)) {
        return true;
      }
    }
    return false;
  }

  protected void setupPatches() {
    for (Patch<T> patch : patches) {
      for (Operation<T> operation : patch.getOperations()) {
        switch (operation.getType()) {
          case ADD:
            nodes.put(operation.getId(), patch.getNode(operation.getId()));
            break;
          case REMOVE:
            nodes.remove(operation.getId());
            break;
          case BUMP_VERSION:
          default:
        }
      }

      List<Operation<T>> operations = patch.getOperations();
      for (Operation<T> operation : operations) {
        switch (operation.getType()) {
          case ADD:
            Node<T> node = patch.getNode(operation.getId());
            nodes.put(operation.getId(), node);
            break;

          case REMOVE:
            nodes.remove(operation.getId());
            break;

          case BUMP_VERSION:
            nodesByVersion.put(patch.getVersion(), new HashMap<>(nodes));
            break;

          default:
        }
      }
    }
  }

  protected void apply(Patch<T> patch) {
    boolean update = patch.getVersion() > configuration.getInt(VERSION_FIELD);
    List<Operation<T>> operations = patch.getOperations();
    int previousVersion = patch.getVersion() - 1;

    // When path version is 1 this will be null but that's fine because this is only used during remove operations
    Map<T, Node<T>> availableNodes = nodesByVersion.get(previousVersion);
    for(Operation<T> operation : operations) {
      switch (operation.getType()) {
        case ADD:
          Node<T> node = patch.getNode(operation.getId());

          if(update) {
            Object value;
            if (node.getDefaultValue().getClass().isEnum()) {
              value = ((Enum) node.getDefaultValue()).name().toUpperCase(Locale.ENGLISH);
            } else {
              value = node.getDefaultValue();
            }
            configuration.set(node.getPath(), value);
          }
          break;

        case REMOVE:
          if(update) {
            configuration.set(availableNodes.get(operation.getId()).getPath(), null);
          }
          break;

        case BUMP_VERSION:
          if(update) {
            configuration.set(VERSION_FIELD, patch.getVersion());
          }
          break;
        default:
      }
    }
  }
}
