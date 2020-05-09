package com.github.hornta.versioned_config;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class NodeMap<T extends Enum<T>> implements Iterable<Node<T>> {
  private final LinkedHashMap<T, Node<T>> nodes;

  NodeMap() {
    this.nodes = new LinkedHashMap<>();
  }

  NodeMap(NodeMap<T> nodeMap) {
    this.nodes = (LinkedHashMap<T, Node<T>>)nodeMap.nodes.clone();
  }

  public void add(Node<T> node) {
    this.nodes.put(node.getId(), node);
  }

  public void remove(T id) {
    this.nodes.remove(id);
  }

  public boolean contains(T id) {
    return this.nodes.containsKey(id);
  }

  public Node<T> get(T id) {
    return this.nodes.get(id);
  }

  @Override
  public Iterator<Node<T>> iterator() {
    return nodes.values().iterator();
  }
}
