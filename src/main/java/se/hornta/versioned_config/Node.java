package se.hornta.versioned_config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

public class Node<T extends Enum<T>> {
  private final T id;
  private final String path;
  private final Object defaultValue;
  private final Type type;
  private final Function<Object, Object> converter;
  private Object currentValue;

  public Node(T id, String path, Object defaultValue, Type type) {
    this.id = id;
    this.path = path;
    this.defaultValue = defaultValue;
    this.type = type;
    this.converter = null;
    this.currentValue = defaultValue;
  }

  public Node(T id, String path, Object defaultValue, Type type, Function<Object, Object> converter) {
    this.id = id;
    this.path = path;
    this.defaultValue = defaultValue;
    this.type = type;
    this.converter = converter;
    this.currentValue = defaultValue;
  }

  public T getId() {
    return id;
  }

  public String getPath() {
    return path;
  }

  public Object getDefaultValue() {
    return defaultValue;
  }

  public Type getType() {
    return type;
  }

  public Function<Object, Object> getConverter() {
    return converter;
  }

  public Object getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(Object currentValue) {
    this.currentValue = currentValue;
  }

  public boolean isExpectedType(ConfigurationSection configurationSection) {
    boolean isExpectedType = false;

    switch (type) {
      case LIST:
        isExpectedType = configurationSection.isList(path);
        break;
      case LONG:
        isExpectedType = configurationSection.isLong(path);
        break;
      case COLOR:
        isExpectedType = configurationSection.isColor(path);
        break;
      case STRING:
        isExpectedType = configurationSection.isString(path);
        break;
      case VECTOR:
        isExpectedType = configurationSection.isVector(path);
        break;
      case BOOLEAN:
        isExpectedType = configurationSection.isBoolean(path);
        break;
      case INTEGER:
        isExpectedType = configurationSection.isInt(path);
        break;
      case ITEM_STACK:
        isExpectedType = configurationSection.isItemStack(path);
        break;
      case OFFLINE_PLAYER:
        isExpectedType = configurationSection.isOfflinePlayer(path);
        break;
      case DOUBLE:
        isExpectedType = configurationSection.isDouble(path) || configurationSection.isInt(path);
      default:
    }

    return isExpectedType;
  }
}
