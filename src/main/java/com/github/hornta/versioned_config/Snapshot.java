package com.github.hornta.versioned_config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Locale;
import java.util.function.Function;

public class Snapshot<T extends Enum<T>> {
  private NodeMap<T> nodes;
  private final YamlConfiguration configuration;

  Snapshot(NodeMap<T> nodes, YamlConfiguration configuration) {
    this.nodes = nodes;
    this.configuration = new YamlConfiguration();
    try {
      this.configuration.loadFromString(configuration.saveToString());
    } catch (InvalidConfigurationException ignored) { }
  }

  public <E> E get(T key) {
    if (!nodes.contains(key)) {
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

  protected YamlConfiguration getConfiguration() {
    return configuration;
  }

  protected NodeMap<T> getNodes() {
    return nodes;
  }

  protected void update(Patch<T> patch) {
    for(Operation<T> operation : patch.getOperations()) {
      switch (operation.getType()) {
        case ADD:
          addNodeToConfiguration(patch.getNode(operation.getId()));
          break;

        case REMOVE:
          removeNodeFromConfiguration(operation.getId());
          break;
        default:
      }
    }
  }

  protected void applyConfiguration(YamlConfiguration configuration) {
    for(Node<T> node : nodes) {
      Object value = configuration.get(node.getPath());
      node.setCurrentValue(value);
      this.configuration.set(node.getPath(), value);
    }
  }

  private void addNodeToConfiguration(Node<T> node) {
    Object value;
    if (node.getDefaultValue().getClass().isEnum()) {
      value = ((Enum) node.getDefaultValue()).name().toUpperCase(Locale.ENGLISH);
    } else {
      value = node.getDefaultValue();
    }
    configuration.set(node.getPath(), value);
    nodes.add(node);
  }

  private void removeNodeFromConfiguration(T id) {
    configuration.set(nodes.get(id).getPath(), null);
    nodes.remove(id);
  }
}
