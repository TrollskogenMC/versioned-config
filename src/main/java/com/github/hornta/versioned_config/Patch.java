package com.github.hornta.versioned_config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Patch<T extends Enum<T>> {
  private final Map<T, Node<T>> nodes;
  private final List<Operation<T>> operations;
  private String validationError;

  public Patch() {
    this.nodes = new HashMap<>();
    this.operations = new LinkedList<>();
  }

  public void set(T id, String path, Object value, Type type) {
    Node<T> node = new Node<>(id, path, value, type);
    nodes.put(id, node);
    operations.add(new Operation<>(Operation.Type.ADD, id));
  }

  public void unset(T id) {
    operations.add(new Operation<>(Operation.Type.REMOVE, id));
  }

  protected Node<T> getNode(T id) {
    return nodes.get(id);
  }

  protected String getValidationError() {
    return validationError;
  }

  protected boolean validate() {
    for(Map.Entry<T, Node<T>> entry : nodes.entrySet()) {
      if(
        entry.getValue().getPath() == null ||
        entry.getValue().getPath().length() == 0
      ) {
        validationError = "The path of " + entry.getKey().name() + " must not be null or empty.";
        return false;
      }

      for (char character : entry.getValue().getPath().toCharArray()) {
        if (Character.getType(character) == Character.UPPERCASE_LETTER) {
          validationError = "The path of " + entry.getKey().name() + " must only contain lowercase characters.";
          return false;
        }
      }

      if(entry.getValue().getPath().equals(Configuration.VERSION_FIELD)) {
        validationError = "The path of " + entry.getKey().name() + "can not override version.";
        return false;
      }
    }
    return true;
  }

  protected List<Operation<T>> getOperations() {
    return operations;
  }
}
