package com.github.hornta.versioned_config.operations;

import com.github.hornta.versioned_config.Operation;

public class RemoveOperation<T extends Enum<T>> extends Operation<T> {
  public RemoveOperation(T id) {
    super(Type.REMOVE, id);
  }
}
