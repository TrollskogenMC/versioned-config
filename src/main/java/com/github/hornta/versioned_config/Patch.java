package com.github.hornta.versioned_config;

import com.github.hornta.versioned_config.operations.AddOperation;
import com.github.hornta.versioned_config.operations.RemoveOperation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Patch<T extends Enum<T>> {
  private final Map<T, Node<T>> nodes;
  private final List<Operation<T>> operations;

  public Patch() {
    this.nodes = new HashMap<>();
    this.operations = new LinkedList<>();
  }

  public void set(T id, String path, Object value, Type type) {
    Node<T> node = new Node<>(id, path, value, type);
    nodes.put(id, node);
    addOperation(new AddOperation<>(id));
  }

  public void unset(T id) {
    addOperation(new RemoveOperation<>(id));
  }

  public void addOperation(Operation<T> operation) {
    operations.add(operation);
  }

  protected Node<T> getNode(T id) {
    return nodes.get(id);
  }

  protected List<Operation<T>> getOperations() {
    return operations;
  }
}
