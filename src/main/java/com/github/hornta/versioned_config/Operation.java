package com.github.hornta.versioned_config;

public class Operation<T extends Enum<T>> {
  private final Type type;
  private final T id;

  public Operation(Type type, T id) {
    this.type = type;
    this.id = id;
  }

  public Operation(Type type) {
    this.type = type;
    this.id = null;
  }

  public Type getType() {
    return type;
  }

  public T getId() {
    return id;
  }

  protected enum Type {
    ADD,
    REMOVE,
    BUMP_VERSION
  }
}
