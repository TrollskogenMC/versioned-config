package com.github.hornta.versioned_config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Patch<T extends Enum<T>> {
  private final Map<T, Node<T>> nodes;
  private final List<Operation<T>> operations;
  private final int version;

  public Patch(int version) {
    this.nodes = new HashMap<>();
    this.operations = new LinkedList<>();
    this.operations.add(new Operation<>(Operation.Type.BUMP_VERSION));
    this.version = version;
  }

  public void set(T id, String path, Object value, Type type) {
    Node<T> node = new Node<>(id, path, value, type);
    nodes.put(id, node);
    operations.add(operations.size() - 1, new Operation<>(Operation.Type.ADD, id));
  }

  public void unset(T id) {
    operations.add(operations.size() - 1, new Operation<>(Operation.Type.REMOVE, id));
  }

  public int getVersion() {
    return version;
  }

  protected Node<T> getNode(T id) {
    return nodes.get(id);
  }

  protected String validate() throws ConfigurationException {
    for(Map.Entry<T, Node<T>> entry : nodes.entrySet()) {
      if(
        entry.getValue().getPath() == null ||
        entry.getValue().getPath().length() == 0
      ) {
        throw new ConfigurationException("The path of %s must not be null or empty.", entry.getKey().name());
      }

      for (char character : entry.getValue().getPath().toCharArray()) {
        if (Character.getType(character) == Character.UPPERCASE_LETTER) {
          throw new ConfigurationException("The path of %s must only contain lowercase characters.", entry.getKey().name());
        }
      }

      if(entry.getValue().getPath().equals(Configuration.VERSION_FIELD)) {
        throw new ConfigurationException("The path of %s can not override version.", entry.getKey().name());
      }
    }
    return null;
  }

  protected List<Operation<T>> getOperations() {
    return operations;
  }
}
