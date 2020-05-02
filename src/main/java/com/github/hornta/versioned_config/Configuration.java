package com.github.hornta.versioned_config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginBase;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;

public class Configuration<T extends Enum<T>> {
  private final PluginBase plugin;
  private final Map<T, Node<T>> nodes;
  private final File file;
  private YamlConfiguration configuration;
  private int version;

  protected static final String VERSION_FIELD = "version";

  public Configuration(PluginBase plugin, File file) {
    this.plugin = plugin;
    this.file = file;
    this.nodes = new LinkedHashMap<>();

    setupFile();
    persistToFile();
  }

  public void persistToFile() {
    try {
      configuration.save(file);
    } catch (IOException ex) {
      plugin.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
      throw new RuntimeException(ex);
    }
  }

  public void reload() {
    setupFile();
    validate();
    cleanup();
    persistToFile();
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

  public Node<T> getNode(String path) {
    for(Node<T> node : nodes.values()) {
      if(node.getPath().equals(path)) {
        return node;
      }
    }
    return null;
  }

  private void setupFile() {
    if (file.exists()) {
      YamlConfiguration config = new YamlConfiguration();
      try {
        config.load(file);
      } catch (Exception ex) {
        plugin.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
        throw new RuntimeException(ex);
      }
      configuration = config;
    } else {
      configuration = new YamlConfiguration();
    }
  }

  protected void cleanup() {
    // try and see if we can delete unused config values
    List<String> keys = new ArrayList<>(configuration.getKeys(true));

    // make sure we reverse the collections so that all leaves ends up first
    Collections.reverse(keys);

    // keys to actually check for being used (leaves)
    Set<String> checkKeys = new HashSet<>();

    for(String key : keys) {
      boolean hasSubstring = false;
      for(String checkKey : checkKeys) {
        if(checkKey.contains(key)) {
          hasSubstring = true;
          break;
        }
      }

      if(hasSubstring) {
        continue;
      }

      checkKeys.add(key);
    }

    checkKeys.remove(VERSION_FIELD);

    for(String path : checkKeys) {
      tryDeletePathRecursively(path);
    }
  }

  private void tryDeletePathRecursively(String path) {
    if(hasPath(path)) {
      return;
    }
    configuration.set(path, null);
    plugin.getLogger().log(Level.WARNING, "Deleted unused path `" + path + "`");
    int separatorIndex = path.lastIndexOf('.');
    if(separatorIndex != -1) {
      tryDeletePathRecursively(path.substring(0, separatorIndex));
    }
  }

  protected void validate() {
    Set<String> errors = new HashSet<>();

    // store keys and values in order defined in ConfigKey so that when saving new keys they end up in order when saving the config.yml
    Map<String, Object> keyValues = new LinkedHashMap<>();

    boolean save = false;
    for (Map.Entry<T, Node<T>> entry : nodes.entrySet()) {
      // try and see if we can add missing config values to the config
      if (!configuration.contains(entry.getValue().getPath())) {
        Object value;
        if(entry.getValue().getDefaultValue().getClass().isEnum()) {
          value = ((Enum<T>)entry.getValue().getDefaultValue()).name().toLowerCase(Locale.ENGLISH);
        } else {
          value = entry.getValue().getDefaultValue();
        }
        keyValues.put(entry.getValue().getPath(), value);
        save = true;
        plugin.getLogger().log(Level.INFO, "Added missing property `" + entry.getValue().getPath() + "` with value `" + value + "`");
        continue;
      }

      keyValues.put(entry.getValue().getPath(), configuration.get(entry.getValue().getPath()));

      // verify that the type in the config file is of the expected type
      boolean isType = entry.getValue().isExpectedType(configuration);

      if(!isType) {
        errors.add("Expected config path \"" + entry.getValue().getPath() + "\" to be of type \"" + entry.getValue().getType().toString() + "\"");
      }
    }

    if(save) {
      // delete everything currently in the config
      for(String key : configuration.getKeys(true)) {
        configuration.set(key, null);
      }

      configuration.set(VERSION_FIELD, version);

      for(Map.Entry<String, Object> entry : keyValues.entrySet()) {
        configuration.set(entry.getKey(), entry.getValue());
      }
    }

    if(!errors.isEmpty()) {
      plugin.getLogger().log(Level.SEVERE, "*** config.yml contains bad values ***");
      errors
        .stream()
        .map((String s) -> "*** " + s + " ***")
        .forEach(plugin.getLogger()::severe);
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

  protected void apply(Patch<T> patch) {
    List<Operation<T>> operations = patch.getOperations();
    for(Operation<T> operation : operations) {
      Node<T> node = patch.getNode(operation.getId());
      if(operation.getType() == Operation.Type.ADD) {
        nodes.put(operation.getId(), node);
      } else if(operation.getType() == Operation.Type.REMOVE) {
        nodes.remove(operation.getId());
      }
    }
  }

  protected void applyValues() {
    for(Node<T> node : nodes.values()) {
      if(configuration.isSet(node.getPath())) {
        node.setCurrentValue(configuration.get(node.getPath()));
      }
    }
  }

  protected void setVersion(int version) {
    this.version = version;
  }

  protected int getVersion() {
    return version;
  }
}
