package com.github.hornta.versioned_config.operations;

import com.github.hornta.versioned_config.Operation;

public class AddOperation<T extends Enum<T>> extends Operation<T> {
  public AddOperation(T id) {
    super(Type.ADD, id);
  }
}
